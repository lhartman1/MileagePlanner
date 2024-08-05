package com.example.mileageplanner.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mileageplanner.R
import com.example.mileageplanner.utils.getMonday
import com.example.mileageplanner.utils.getSunday
import com.example.mileageplanner.utils.getWeek
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val SLIDER_DEFAULT_MAX = 26

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")

@OptIn(PreviewOnly::class)
@Preview(showBackground = true)
@Composable
private fun MileageSliderGroupPreview() {
    MileageSliderGroup(LocalDate.now(), MileageViewModelPreviewImpl())
}

@Composable
fun MileageSliderGroup(
    dayInWeek: LocalDate,
    viewModel: MileageViewModel,
    modifier: Modifier = Modifier,
) {
    val mileageList = viewModel.getMileageValues(dayInWeek)
        .collectAsState(initial = emptyList()).value
    val totalMileage = mileageList.fold(BigDecimal.ZERO) { acc, dayMileage ->
        acc + dayMileage.mileage
    }.toPlainString()

    Column(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val monday = dayInWeek.getMonday()
        val isCurrentWeek = monday == LocalDate.now().getMonday()
        val mondayStr = monday.format(dateTimeFormatter)
        val sundayStr = dayInWeek.getSunday().format(dateTimeFormatter)
        Text(
            text = "$mondayStr to $sundayStr",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = if (isCurrentWeek) MaterialTheme.colorScheme.primary else Color.Unspecified
            ),
        )

        Row(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 24.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { viewModel.copyWeek(mileageList) },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_content_copy_24),
                    contentDescription = "Copy week",
                )
            }
            Text(
                text = "Total: $totalMileage",
                style = MaterialTheme.typography.headlineMedium,
            )
            IconButton(
                onClick = { viewModel.pasteWeek(dayInWeek.getWeek()) },
                enabled = viewModel.copiedWeekStateFlow.collectAsState().value.isNotEmpty(),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_content_paste_24),
                    contentDescription = "Pastes copied week",
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            mileageList.forEach { dayMileage ->
                MileageSlider(dayMileage = dayMileage, sliderMax = SLIDER_DEFAULT_MAX) {
                    val newDayMileage = dayMileage.copy(mileage = it.toBigDecimal())
                    viewModel.updateMileage(newDayMileage)
                }
            }
        }
    }
}
