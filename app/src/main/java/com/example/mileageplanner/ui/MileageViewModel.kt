package com.example.mileageplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileageplanner.data.DayMileage
import com.example.mileageplanner.data.MileageRepository
import com.example.mileageplanner.utils.getWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.annotation.AnnotationTarget.CLASS

class MileageViewModelImpl(private val repository: MileageRepository) :
    ViewModel(), MileageViewModel {

    private val _copiedWeekMutableStateFlow = MutableStateFlow(emptyList<DayMileage>())
    override val copiedWeekStateFlow: StateFlow<List<DayMileage>> =
        _copiedWeekMutableStateFlow.asStateFlow()

    override fun copyWeek(week: List<DayMileage>) {
        _copiedWeekMutableStateFlow.value = week
    }

    @Suppress("SpreadOperator")
    override fun pasteWeek(week: List<LocalDate>) {
        val newWeek = week.map { day ->
            val newMileage = copiedWeekStateFlow.value
                .firstOrNull { it.day.dayOfWeek == day.dayOfWeek }?.mileage ?: BigDecimal.ZERO
            DayMileage(day, newMileage)
        }
        updateMileage(*newWeek.toTypedArray())
    }

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
    override val copiedWeekStateFlow: StateFlow<List<DayMileage>> =
        MutableStateFlow(emptyList<DayMileage>()).asStateFlow()

    override fun copyWeek(week: List<DayMileage>) {
        // no-op
    }

    override fun pasteWeek(week: List<LocalDate>) {
        // no-op
    }

    override fun getAllMileageValues(): Flow<List<DayMileage>> = flowOf(MileageViewModel.INITIAL)

    override fun getMileageValues(date: LocalDate): Flow<List<DayMileage>> =
        flowOf(MileageViewModel.INITIAL)

    override fun updateMileage(vararg dayMileage: DayMileage) {
        // no-op
    }
}

interface MileageViewModel {

    val copiedWeekStateFlow: StateFlow<List<DayMileage>>

    fun copyWeek(week: List<DayMileage>)

    fun pasteWeek(week: List<LocalDate>)

    fun getAllMileageValues(): Flow<List<DayMileage>>

    fun getMileageValues(date: LocalDate): Flow<List<DayMileage>>

    fun updateMileage(vararg dayMileage: DayMileage)

    companion object {
        val INITIAL get() = LocalDate.now().getWeek().map { DayMileage(it, BigDecimal.ZERO) }
    }
}

@RequiresOptIn(message = "This is to be used in Compose functions that are for Preview only.")
@Retention(AnnotationRetention.BINARY)
@Target(CLASS)
annotation class PreviewOnly
