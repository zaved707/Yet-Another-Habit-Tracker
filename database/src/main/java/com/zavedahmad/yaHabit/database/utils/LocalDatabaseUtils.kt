package com.zavedahmad.yaHabit.database.utils

import android.content.Context
import androidx.room.Room
import com.zavedahmad.yaHabit.database.MainDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class LocalDatabaseUtils @Inject constructor(
    @ApplicationContext val context: Context,
    private val habitDatabase: MainDatabase
) : DatabaseUtils{
    override fun isDatabaseValid(): Boolean {
        val testDatabase: MainDatabase=
            Room.databaseBuilder(context, MainDatabase::class.java, "main_database")

                .build()
        try {
            testDatabase.openHelper.writableDatabase
        } catch (e: Exception) {
            testDatabase.close()
            return false
        }
        testDatabase.close()
        return true
    }

    override fun getDatabasePath(): File {
        return context.getDatabasePath("main_database")
    }

    override fun closeDatabase() {
        if (habitDatabase.isOpen)
            habitDatabase.close()
    }

    override fun isDatabaseOpen(): Boolean {
        return habitDatabase.isOpen
    }

}