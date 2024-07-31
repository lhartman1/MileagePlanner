package com.example.mileageplanner.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mileageplanner.MileagePlannerApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for MileageViewModel
//        initializer {
//            MileageViewModel(
//                mileagePlannerApplication().container.mileageRepository
//            )
//        }
    }
}

fun CreationExtras.mileagePlannerApplication() : MileagePlannerApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MileagePlannerApplication
