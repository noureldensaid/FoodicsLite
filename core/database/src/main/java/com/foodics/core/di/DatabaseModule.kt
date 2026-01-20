package com.foodics.core.di

import androidx.room.Room
import com.foodics.core.common.util.Constants
import com.foodics.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().productDao() }
}