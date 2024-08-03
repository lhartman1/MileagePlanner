package com.example.mileageplanner.data

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun mileageFromInt(value: Int?): BigDecimal? {
        return value?.let { BigDecimal(it) / BigDecimal.TEN }
    }

    @TypeConverter
    fun intFromMileage(mileage: BigDecimal?): Int?{
        return mileage?.let { (it * BigDecimal.TEN).toInt() }
    }
}
