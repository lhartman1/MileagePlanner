package com.example.mileageplanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val MILEAGE_DATABASE_NAME = "mileage_database"

@Database(entities = [DayMileage::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class MileageDatabase : RoomDatabase() {

    abstract fun dayMileageDao(): DayMileageDao

    companion object {
        @Volatile
        private var Instance: MileageDatabase? = null

        fun getDatabase(context: Context): MileageDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MileageDatabase::class.java, MILEAGE_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
