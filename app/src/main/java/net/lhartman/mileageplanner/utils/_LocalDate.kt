package net.lhartman.mileageplanner.utils

import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

const val DAYS_IN_WEEK = 7

fun LocalDate.getMonday(): LocalDate = minusDays(dayOfWeek.ordinal.toLong())

fun LocalDate.getSunday(): LocalDate = plusDays((DAYS_IN_WEEK - dayOfWeek.value).toLong())

fun LocalDate.getWeek(): List<LocalDate> {
    val monday = getMonday()
    return List(DAYS_IN_WEEK) { monday.plusDays(it.toLong()) }
}

fun LocalDate.getShortDisplayName(): String = dayOfWeek.getDisplayName(
    TextStyle.SHORT_STANDALONE,
    Locale.getDefault(),
)
