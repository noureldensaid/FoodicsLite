package com.foodics.nour.ui

import android.app.Application
import com.foodics.core.di.databaseModule
import com.foodics.core.di.networkModule
import com.foodics.feature.tables.ui.di.tablesUiModule
import com.foodics.nour.di.mainAppModule
import com.foodics.tables.data.di.tablesDataModule
import com.foodics.tables.domain.di.tablesDomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                networkModule,
                databaseModule,
                mainAppModule,

                // tables feature modules
                tablesDataModule,
                tablesDomainModule,
                tablesUiModule
            )
        }
    }
}