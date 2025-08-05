package com.zavedahmad.yaHabit.database.utils

import java.io.File



    interface DatabaseUtils {

        fun isDatabaseValid(): Boolean

        fun getDatabasePath(): File

        fun closeDatabase()

        fun isDatabaseOpen(): Boolean


}