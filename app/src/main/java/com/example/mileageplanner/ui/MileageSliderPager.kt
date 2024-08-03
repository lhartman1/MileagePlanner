package com.example.mileageplanner.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mileageplanner.data.getCurrentWeekNumber
import com.example.mileageplanner.data.getNthMonday
import com.example.mileageplanner.data.getNumberOfWeeks
import com.example.mileageplanner.utils.getMonday
import java.time.LocalDate
import kotlin.math.absoluteValue

@Preview(showBackground = true)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MileageSliderPager(
    modifier: Modifier = Modifier,
    viewModel: MileageViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val allMileage = viewModel.getAllMileageValues().collectAsState(initial = emptyList())
    val pagerState = rememberPagerState(
        // Adding 2 to page count to make an empty week at the beginning and end of the pager
        pageCount = { allMileage.value.getNumberOfWeeks().toInt() + 2 },
        // Offsetting initial page by 1 because `getCurrentWeekNumber` doesn't account for the
        // beginning page added to pageCount above
        initialPage = allMileage.value.getCurrentWeekNumber().toInt() + 1,
    )

    HorizontalPager(
        modifier = modifier.fillMaxSize(),
        state = pagerState,
        contentPadding = PaddingValues(24.dp),
        pageSpacing = 8.dp,
    ) { page ->
        Box(
            modifier = Modifier
                .graphicsLayer {
                    // Calculate the absolute offset for the current page from the
                    // scroll position. We use the absolute value which allows us to mirror
                    // any effects for both directions
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                    // We animate the alpha, between 50% and 100%
                    alpha = lerp(
                        start = 0.75f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            // Offsetting by -1 because `getNthMonday` doesn't account for the beginning page added
            // to the pager's pageCount
            val paginatedMonday = allMileage.value.getNthMonday(page - 1)
                ?: LocalDate.now().getMonday()
            // TODO: This may be causing a bug when editing the first week
            MileageSliderGroup(dayInWeek = paginatedMonday)
        }
    }
}
