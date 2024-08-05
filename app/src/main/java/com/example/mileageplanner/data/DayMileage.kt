package com.example.mileageplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mileageplanner.utils.DAYS_IN_WEEK
import com.example.mileageplanner.utils.getMonday
import java.math.BigDecimal
import java.time.LocalDate

const val DAY_MILEAGE_TABLE = "day_miles"

@Entity(tableName = DAY_MILEAGE_TABLE)
data class DayMileage(
    @PrimaryKey
    val day: LocalDate,

    val mileage: BigDecimal,
)

fun List<DayMileage>.getNumberOfWeeks(): Long {
    if (size <= 1) return 1
    val firstMonday = first().day.getMonday().toEpochDay()
    val lastMonday = last().day.getMonday().toEpochDay()
    val numberOfWeeks = ((lastMonday - firstMonday) / DAYS_IN_WEEK) + 1
    return numberOfWeeks
}

fun List<DayMileage>.getNthMonday(n: Int): LocalDate? {
    return firstOrNull()?.day?.getMonday()?.plusWeeks(n.toLong())
}

fun List<DayMileage>.getCurrentWeekNumber(): Long {
    val thisMonday = LocalDate.now().getMonday().toEpochDay()
    val firstMonday = firstOrNull()?.day?.getMonday()?.toEpochDay() ?: thisMonday
    val weekNumber = (thisMonday - firstMonday) / DAYS_IN_WEEK
    return weekNumber
}
