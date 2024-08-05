package com.example.mileageplanner.ui

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.example.mileageplanner.data.DayMileage
import com.example.mileageplanner.utils.getShortDisplayName
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
private fun MileageSliderPreview() {
    MileageSlider(
        dayMileage = DayMileage(LocalDate.now(), BigDecimal.ONE),
        sliderMax = 10,
    ) {}
}

private const val SLIDER_HEIGHT = 256

private const val ROTATE_270 = 270f

/**
 * To get the Slider to be vertical, I used this resource:
 * https://stackoverflow.com/a/71129399
 */
@Composable
fun MileageSlider(
    dayMileage: DayMileage,
    @IntRange(from = 1) sliderMax: Int,
    modifier: Modifier = Modifier,
    onValueChanged: (Int) -> Unit,
) {
    val dayString = dayMileage.day.getShortDisplayName()
    val isToday = dayMileage.day == LocalDate.now()
    val mileage = dayMileage.mileage.toInt()
    val steps = sliderMax - 1

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = dayString,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = if (isToday) MaterialTheme.colorScheme.primary else Color.Unspecified
            ),
        )
        Text(
            text = "$mileage",
            style = MaterialTheme.typography.bodyMedium,
        )
        Slider(
            value = mileage.toFloat(),
            onValueChange = { onValueChanged(it.roundToInt()) },
            valueRange = 0.0f..sliderMax.toFloat(),
            steps = steps,
            colors = SliderDefaults.colors().copy(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            ),
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = ROTATE_270
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = constraints.minHeight,
                            maxWidth = constraints.maxHeight,
                            minHeight = constraints.minWidth,
                            maxHeight = constraints.maxHeight,
                        )
                    )
                    layout(placeable.height, placeable.width) {
                        placeable.place(-placeable.width, 0)
                    }
                }
                .width(SLIDER_HEIGHT.dp) // width is actually height because of the transformation
        )
    }
}
