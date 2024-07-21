package com.example.mileageplanner.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun MileageSliderGroup(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        DayOfWeek.entries.forEach { dayOfWeek ->
            val dayString = dayOfWeek.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())
            MileageSlider(day = dayString, sliderMax = 10)
        }
    }
}
