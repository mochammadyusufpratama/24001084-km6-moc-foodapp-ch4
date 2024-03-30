package com.app.makanku

import android.app.Application
import com.app.makanku.data.source.local.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}