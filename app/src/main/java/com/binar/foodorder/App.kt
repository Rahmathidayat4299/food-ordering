package com.binar.foodorder

import android.app.Application
import com.binar.foodorder.data.local.database.AppDatabase

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}