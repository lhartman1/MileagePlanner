package com.example.mileageplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileageplanner.data.DayMileage
import com.example.mileageplanner.data.MileageRepository
import com.example.mileageplanner.utils.getWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

class MileageViewModel(private val repository: MileageRepository) : ViewModel() {

    internal fun getMileageValues(date: LocalDate = LocalDate.now()): Flow<List<DayMileage>> {
        return repository.getWeekMileage(date).transform { mileageList ->
            val mutableMileageList = mileageList.toMutableList()
            val week = date.getWeek()
            for (day in week) {
                if (mutableMileageList.none { it.day == day }) {
                    mutableMileageList.add(day.dayOfWeek.ordinal, DayMileage(day, BigDecimal.ZERO))
                }
            }
            emit(mutableMileageList)
        }
    }

    fun updateMileage(dayMileage: DayMileage) {
        viewModelScope.launch {
            repository.insertDayMileage(dayMileage)
        }
    }
}
