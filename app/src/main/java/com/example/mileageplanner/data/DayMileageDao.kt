package com.example.mileageplanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DayMileageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dayMileage: DayMileage)

    @Update
    suspend fun update(dayMileage: DayMileage)

    @Delete
    suspend fun delete(dayMileage: DayMileage)

    @Query("SELECT * from $DAY_MILEAGE_TABLE WHERE day = :date")
    fun getDayMileage(date: LocalDate): Flow<DayMileage>

    @Query("SELECT * from $DAY_MILEAGE_TABLE WHERE day >= :startOfWeek AND day <= :endOfWeek")
    fun getWeekMileage(startOfWeek: LocalDate, endOfWeek: LocalDate): Flow<List<DayMileage>>

    @Query("SELECT * from $DAY_MILEAGE_TABLE ORDER BY day ASC")
    fun getAllDays(): Flow<List<DayMileage>>
}
