package com.zavedahmad.yaHabit.database.utils

import android.content.Context
import androidx.room.Room
import com.zavedahmad.yaHabit.database.MainDatabase

import java.io.File


class LocalDatabaseUtils(
    private val context: Context,
    private val habitDatabase: MainDatabase
) : DatabaseUtils{
    override fun isDatabaseValid(): Boolean {
        val testDatabase: MainDatabase=
            Room.databaseBuilder(context, MainDatabase::class.java, "main_database")
//                .addMigrations(MIGRATION_1_2)    //Todo this i migration
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