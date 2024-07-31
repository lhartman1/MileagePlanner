package com.example.mileageplanner.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

class OfflineMileageRepository(private val dayMileageDao: DayMileageDao) : MileageRepository {
    override fun getAllStream(): Flow<List<DayMileage>> = dayMileageDao.getAllDays()

    override fun getDayMileage(date: Date): Flow<DayMileage?> = dayMileageDao.getDayMileage(date)

    override suspend fun insertDayMileage(dayMileage: DayMileage) = dayMileageDao.insert(dayMileage)

    override suspend fun updateDayMileage(dayMileage: DayMileage) = dayMileageDao.update(dayMileage)

    override suspend fun deleteDayMileage(dayMileage: DayMileage) = dayMileageDao.delete(dayMileage)
}
