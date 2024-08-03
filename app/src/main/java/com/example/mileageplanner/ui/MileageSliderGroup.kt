package com.example.mileageplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.math.BigDecimal
import java.time.format.TextStyle
import java.util.Locale

private const val SLIDER_DEFAULT_MAX = 26

@Preview(showBackground = true)
@Composable
fun MileageSliderGroup(
    modifier: Modifier = Modifier,
    viewModel: MileageViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val mileageList = viewModel.getMileageValues().collectAsState(initial = emptyList())
    val totalMileage = mileageList.value.fold(BigDecimal.ZERO) { acc, dayMileage ->
        acc + dayMileage.mileage
    }.toPlainString()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Total: $totalMileage",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            mileageList.value.forEach { dayMileage ->
                val dayString = dayMileage.day.dayOfWeek.getDisplayName(
                    TextStyle.SHORT_STANDALONE,
                    Locale.getDefault(),
                )
                val mileage = dayMileage.mileage.toInt()

                MileageSlider(day = dayString, mileage = mileage, sliderMax = SLIDER_DEFAULT_MAX) {
                    val newDayMileage = dayMileage.copy(mileage = it.toBigDecimal())
                    viewModel.updateMileage(newDayMileage)
                }
            }
        }
    }
}
