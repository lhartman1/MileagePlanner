package net.lhartman.mileageplanner.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import net.lhartman.mileageplanner.MileagePlannerApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for MileageViewModelImpl
        initializer {
            MileageViewModelImpl(
                mileagePlannerApplication().container.mileageRepository,
            )
        }
    }
}

fun CreationExtras.mileagePlannerApplication(): MileagePlannerApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MileagePlannerApplication
