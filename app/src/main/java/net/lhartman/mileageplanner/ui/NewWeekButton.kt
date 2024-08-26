package net.lhartman.mileageplanner.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import net.lhartman.mileageplanner.data.DayMileage
import net.lhartman.mileageplanner.utils.getMonday
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class, PreviewOnly::class)
@Preview(showBackground = true)
@Composable
private fun NewWeekButtonPreview() {
    NewWeekButton(
        viewModel = MileageViewModelPreviewImpl(),
        paginatedMonday = LocalDate.now().getMonday(),
        pagerState = rememberPagerState(pageCount = { 1 }),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewWeekButton(
    viewModel: MileageViewModel,
    paginatedMonday: LocalDate,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    Box {
        IconButton(
            onClick = {
                viewModel.updateMileage(
                    DayMileage(paginatedMonday, BigDecimal.ZERO)
                )
                coroutineScope.launch {
                    pagerState.scrollToPage(1)
                }
            },
            modifier = Modifier.scale(2f),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }
    }
}
