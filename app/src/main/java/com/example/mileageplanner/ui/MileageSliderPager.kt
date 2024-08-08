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
import com.example.mileageplanner.data.DayMileage
import com.example.mileageplanner.data.getCurrentWeekNumber
import com.example.mileageplanner.data.getNthMonday
import com.example.mileageplanner.data.getNumberOfWeeks
import com.example.mileageplanner.utils.getMonday
import java.time.LocalDate
import kotlin.math.absoluteValue

@OptIn(PreviewOnly::class)
@Preview(showBackground = true)
@Composable
private fun MileageSliderPagerPreview() {
    ComposeLocalWrapper {
        MileageSliderPager(
            viewModel = MileageViewModelPreviewImpl(),
            initialState = MileageViewModel.INITIAL,
        )
    }
}

/**
 * @param initialState should be null so that function can be returned early and the pagerState is
 * not created until the first value is emitted from the flow. It is added as a param to allow the
 * Compose Preview to work.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MileageSliderPager(
    viewModel: MileageViewModel,
    modifier: Modifier = Modifier,
    initialState: List<DayMileage>? = null,
) {
    val allMileage = viewModel.getAllMileageValues().collectAsState(initial = initialState).value
        ?: return

    val pagerState = rememberPagerState(
        // Adding 2 to page count to make an empty week at the beginning and end of the pager
        pageCount = { allMileage.getNumberOfWeeks().toInt() + 2 },
        // Offsetting initial page by 1 because `getCurrentWeekNumber` doesn't account for the
        // beginning page added to pageCount above
        initialPage = allMileage.getCurrentWeekNumber().toInt() + 1,
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
            val paginatedMonday = allMileage.getNthMonday(page - 1)
                ?: LocalDate.now().getMonday().plusWeeks((page - 1).toLong())
            when {
                page == 0 -> NewWeekButton(viewModel, paginatedMonday, pagerState)
                else -> MileageSliderGroup(paginatedMonday, viewModel)
            }
        }
    }
}
