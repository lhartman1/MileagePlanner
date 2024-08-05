package com.example.mileageplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileageplanner.data.DayMileage
import com.example.mileageplanner.data.MileageRepository
import com.example.mileageplanner.utils.getWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.annotation.AnnotationTarget.CLASS

class MileageViewModelImpl(private val repository: MileageRepository) : ViewModel(),
    MileageViewModel {

    override fun getAllMileageValues(): Flow<List<DayMileage>> = repository.getAllStream()

    override fun getMileageValues(date: LocalDate): Flow<List<DayMileage>> {
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

    override fun updateMileage(vararg dayMileage: DayMileage) {
        viewModelScope.launch {
            repository.insertDayMileage(*dayMileage)
        }
    }
}

@PreviewOnly
class MileageViewModelPreviewImpl : MileageViewModel {
    override fun getAllMileageValues(): Flow<List<DayMileage>> = flowOf(MileageViewModel.INITIAL)

    override fun getMileageValues(date: LocalDate): Flow<List<DayMileage>> =
        flowOf(MileageViewModel.INITIAL)

    override fun updateMileage(vararg dayMileage: DayMileage) {}
}

interface MileageViewModel {

    companion object {
        val INITIAL = LocalDate.now().getWeek().map { DayMileage(it, BigDecimal.ZERO) }
    }

    fun getAllMileageValues(): Flow<List<DayMileage>>

    fun getMileageValues(date: LocalDate): Flow<List<DayMileage>>

    fun updateMileage(vararg dayMileage: DayMileage)
}

@RequiresOptIn(message = "This is to be used in Compose functions that are for Preview only.")
@Retention(AnnotationRetention.BINARY)
@Target(CLASS)
annotation class PreviewOnly
