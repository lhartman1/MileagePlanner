package net.lhartman.mileageplanner

import android.app.Application
import net.lhartman.mileageplanner.data.AppContainer
import net.lhartman.mileageplanner.data.AppDataContainer

class MileagePlannerApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
