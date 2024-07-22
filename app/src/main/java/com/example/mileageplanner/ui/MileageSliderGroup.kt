package com.example.mileageplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mileageplanner.data.PreferencesRepository
import java.time.format.TextStyle
import java.util.Locale

private const val SLIDER_DEFAULT_MAX = 26

@Preview(showBackground = true)
@Composable
fun MileageSliderGroup(
    modifier: Modifier = Modifier,
    viewModel: MileageViewModel = viewModel(
        factory = MileageViewModelFactory(
            PreferencesRepository(
                LocalContext.current
            )
        )
    ),
) {
    val mileageMap = viewModel.mileageValues.collectAsState()
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        mileageMap.value.forEach { (dayOfWeek, mileage) ->
            val dayString =
                dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())

            MileageSlider(day = dayString, mileage = mileage, sliderMax = SLIDER_DEFAULT_MAX) {
                viewModel.updateMileage(dayOfWeek, it)
            }
        }
    }
}
