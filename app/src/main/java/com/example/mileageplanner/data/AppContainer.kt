package com.example.mileageplanner.data

import android.content.Context

interface AppContainer {
    val mileageRepository: MileageRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val mileageRepository: MileageRepository by lazy {
        OfflineMileageRepository(MileageDatabase.getDatabase(context).dayMileageDao())
    }
}
