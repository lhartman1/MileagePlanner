package com.example.mileageplanner

import android.app.Application
import com.example.mileageplanner.data.AppContainer
import com.example.mileageplanner.data.AppDataContainer

class MileagePlannerApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
