package com.example.mileageplanner.data

import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Repository that provides insert, update, delete, and retrieve of [DayMileage] from a given data
 * source.
 */
interface MileageRepository {

    /**
     * Retrieve all the [DayMileage]s from the the given data source.
     */
    fun getAllStream(): Flow<List<DayMileage>>

    /**
     * Retrieve a [DayMileage] from the given data source that matches with the [date].
     */
    fun getDayMileage(date: Date): Flow<DayMileage?>

    /**
     * Insert a [DayMileage] in the data source
     */
    suspend fun insertDayMileage(dayMileage: DayMileage)

    /**
     * Update a [DayMileage] in the data source
     */
    suspend fun updateDayMileage(dayMileage: DayMileage)

    /**
     * Delete a [DayMileage] from the data source
     */
    suspend fun deleteDayMileage(dayMileage: DayMileage)
}
