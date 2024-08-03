package com.example.mileageplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

const val DAY_MILEAGE_TABLE = "day_miles"

@Entity(tableName = DAY_MILEAGE_TABLE)
data class DayMileage(
    @PrimaryKey
    val day: LocalDate,

    val mileage: BigDecimal,
)
