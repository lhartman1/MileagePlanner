package com.example.mileageplanner.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mileageplanner.utils.getMonday
import com.example.mileageplanner.utils.getSunday
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

private const val SLIDER_DEFAULT_MAX = 26

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")

@Preview(showBackground = true)
@Composable
private fun MileageSliderGroupPreview(modifier: Modifier = Modifier) {
    MileageSliderGroup(dayInWeek = LocalDate.now())
}

@Composable
fun MileageSliderGroup(
    dayInWeek: LocalDate,
    modifier: Modifier = Modifier,
    viewModel: MileageViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val mileageList = viewModel.getMileageValues(dayInWeek).collectAsState(initial = emptyList())
    val totalMileage = mileageList.value.fold(BigDecimal.ZERO) { acc, dayMileage ->
        acc + dayMileage.mileage
    }.toPlainString()

    Column(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val mondayStr = dayInWeek.getMonday().format(dateTimeFormatter)
        val sundayStr = dayInWeek.getSunday().format(dateTimeFormatter)
        Text(
            text = "$mondayStr to $sundayStr",
            style = MaterialTheme.typography.headlineSmall,
        )

        Text(
            text = "Total: $totalMileage",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp),
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
