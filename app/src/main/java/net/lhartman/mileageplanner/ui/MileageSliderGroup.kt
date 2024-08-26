package net.lhartman.mileageplanner.ui

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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.lhartman.mileageplanner.R
import net.lhartman.mileageplanner.data.DayMileage
import net.lhartman.mileageplanner.utils.getMonday
import net.lhartman.mileageplanner.utils.getSunday
import net.lhartman.mileageplanner.utils.getWeek
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val SLIDER_DEFAULT_MAX = 26

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d")

@OptIn(PreviewOnly::class)
@Preview(showBackground = true)
@Composable
private fun MileageSliderGroupPreview() {
    ComposeLocalWrapper {
        MileageSliderGroup(LocalDate.now(), MileageViewModelPreviewImpl())
    }
}

@Composable
fun MileageSliderGroup(
    dayInWeek: LocalDate,
    viewModel: MileageViewModel,
    modifier: Modifier = Modifier,
) {
    val mileageList = viewModel.getMileageValues(dayInWeek)
        .collectAsState(initial = emptyList()).value

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

        TotalMileageAndCopyPasteRow(
            dayInWeek = dayInWeek,
            mileageList = mileageList,
            viewModel = viewModel,
        )

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

@Composable
private fun TotalMileageAndCopyPasteRow(
    dayInWeek: LocalDate,
    mileageList: List<DayMileage>,
    viewModel: MileageViewModel,
    modifier: Modifier = Modifier,
) {
    val totalMileage = mileageList.fold(BigDecimal.ZERO) { acc, dayMileage ->
        acc + dayMileage.mileage
    }.toPlainString()
    val snackbarHostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .padding(top = 16.dp, bottom = 24.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = {
                viewModel.copyWeek(mileageList)
                scope.launch {
                    showCopySnackbar(snackbarHostState)
                }
            },
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
            onClick = {
                viewModel.pasteWeek(dayInWeek.getWeek())
                scope.launch {
                    showPasteSnackbar(dayInWeek, mileageList, viewModel, snackbarHostState)
                }
            },
            enabled = viewModel.copiedWeekStateFlow.collectAsState().value.isNotEmpty(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_content_paste_24),
                contentDescription = "Pastes copied week",
            )
        }
    }
}

private suspend fun showCopySnackbar(snackbarHostState: SnackbarHostState) {
    snackbarHostState.currentSnackbarData?.dismiss()
    snackbarHostState.showSnackbar("Week copied!")
}

private suspend fun showPasteSnackbar(
    dayInWeek: LocalDate,
    mileageList: List<DayMileage>,
    viewModel: MileageViewModel,
    snackbarHostState: SnackbarHostState,
) {
    snackbarHostState.currentSnackbarData?.dismiss()
    snackbarHostState.showSnackbar(
        message = "Week pasted!",
        actionLabel = "Undo",
        withDismissAction = true,
    ).also { result ->
        when (result) {
            SnackbarResult.ActionPerformed -> {
                viewModel.copyWeek(mileageList)
                viewModel.pasteWeek(dayInWeek.getWeek())
                viewModel.clearCopiedWeek()
            }
            SnackbarResult.Dismissed -> Unit
        }
    }
}
