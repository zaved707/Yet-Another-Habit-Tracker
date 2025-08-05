package com.zavedahmad.yaHabit.database.repositories

import android.content.Context
import android.net.Uri
import androidx.sqlite.db.SimpleSQLiteQuery
import com.zavedahmad.yaHabit.database.MainDatabase
import com.zavedahmad.yaHabit.database.daos.HabitCompletionDao
import com.zavedahmad.yaHabit.database.daos.HabitDao
import com.zavedahmad.yaHabit.database.utils.DatabaseUtils

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

class ImportExportRepository @Inject constructor(
    val habitDao: HabitDao,
    val habitCompletionDao: HabitCompletionDao,
    val db: MainDatabase,
    @ApplicationContext val context : Context,
    val databaseUtils: DatabaseUtils,
) {
    suspend fun importDatabase(uri: Uri, onComplete: (Result<Unit>) -> Unit){
        withContext(Dispatchers.IO){
            try {
                val databaseFile = databaseUtils.getDatabasePath()

                if (databaseFile.exists()) {
                    val backupDatabaseFile = File.createTempFile("old", ".db")

                    databaseUtils.closeDatabase()
                    if (!databaseUtils.isDatabaseOpen()) {
                        backupDatabaseFile.writeBytes(databaseFile.readBytes())
                        deleteDatabaseFiles(databaseFile)

                        // Copy from Uri input stream
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            databaseFile.writeBytes(input.readBytes())
                        } ?: throw IllegalStateException("Failed to open input stream")
                    } else {
                        throw IllegalStateException("Database could not be closed")
                    }

                    if (!databaseUtils.isDatabaseValid()) {
                        deleteDatabaseFiles(databaseFile)
                        databaseFile.writeBytes(backupDatabaseFile.readBytes())
                        throw IllegalStateException("Invalid database file")
                    }

                    onComplete(Result.success(Unit))
                } else {
                    // If no existing database, still import the new one
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        databaseFile.writeBytes(input.readBytes())
                    } ?: throw IllegalStateException("Failed to open input stream")

                    if (!databaseUtils.isDatabaseValid()) {
                        deleteDatabaseFiles(databaseFile)
                        throw IllegalStateException("Invalid database file")
                    }

                    onComplete(Result.success(Unit))
                }
            } catch (e: Exception) {
                onComplete(Result.failure(e))
            }
        }
    }
    suspend fun exportDatabase(uri: Uri, onComplete: (Result<Unit>) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                // Run checkpoint to ensure WAL is committed to the main database file
                habitDao.rawQuery(SimpleSQLiteQuery("pragma wal_checkpoint(full)"))

                val dbFile = context.getDatabasePath("main_database")
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    FileInputStream(dbFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                } ?: throw IllegalStateException("Failed to open output stream")
                onComplete(Result.success(Unit))
            } catch (e: Exception) {
                onComplete(Result.failure(e))
            }
        }
    }
    private fun deleteDatabaseFiles(databaseFile: File) {
        val databaseDirectory = File(databaseFile.parent!!)
        if (databaseDirectory.isDirectory) {
            databaseDirectory.listFiles()?.forEach { child ->
                if (child.name.endsWith("-shm") || child.name.endsWith("-wal")) {
                    child.deleteRecursively()
                }
            }
        }
        databaseFile.deleteRecursively()
    }
}