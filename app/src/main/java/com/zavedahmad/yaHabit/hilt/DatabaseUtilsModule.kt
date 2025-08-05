package com.zavedahmad.yaHabit.hilt

import com.zavedahmad.yaHabit.database.utils.DatabaseUtils
import com.zavedahmad.yaHabit.database.utils.LocalDatabaseUtils
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DatabaseUtilsModule {

    @Binds
    abstract fun bindDatabaseUtils(
        localDatabaseUtils: LocalDatabaseUtils
    ): DatabaseUtils

}