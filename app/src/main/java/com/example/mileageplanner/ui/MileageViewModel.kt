package com.example.mileageplanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mileageplanner.data.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek

class MileageViewModel(private val preferencesRepository: PreferencesRepository) : ViewModel() {

    private val _mileageValues = MutableStateFlow(getMileageValues())
    val mileageValues = _mileageValues.asStateFlow()

    private fun getMileageValues(): Map<DayOfWeek, Int> =
        DayOfWeek.entries.associateWith { preferencesRepository.getInt(it.name, 0) }

    fun updateMileage(dayOfWeek: DayOfWeek, mileage: Int) {
        val newMap: Map<DayOfWeek, Int> = _mileageValues.value.toMutableMap().apply {
            set(dayOfWeek, mileage)
        }
        _mileageValues.value = newMap
        preferencesRepository.putInt(dayOfWeek.name, mileage)
    }
}

class MileageViewModelFactory(private val preferencesRepository: PreferencesRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MileageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MileageViewModel(preferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
