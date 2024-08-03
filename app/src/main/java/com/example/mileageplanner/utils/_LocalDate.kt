package com.example.mileageplanner.utils

import java.time.LocalDate

fun LocalDate.getMonday(): LocalDate = minusDays(dayOfWeek.ordinal.toLong())

fun LocalDate.getSunday(): LocalDate = plusDays(6L - dayOfWeek.ordinal)

fun LocalDate.getWeek(): List<LocalDate> {
    val monday = getMonday()
    return List(7) { monday.plusDays(it.toLong()) }
}
