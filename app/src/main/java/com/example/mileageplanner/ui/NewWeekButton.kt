package com.example.mileageplanner.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.example.mileageplanner.data.DayMileage
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

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
