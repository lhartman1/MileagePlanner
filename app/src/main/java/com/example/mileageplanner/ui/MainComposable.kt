package com.example.mileageplanner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mileageplanner.ui.theme.MileagePlannerTheme

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No Snackbar Host State")
}

@OptIn(PreviewOnly::class)
@Preview
@Composable
private fun MainComposablePreview() {
    MileagePlannerTheme {
        MainComposable(viewModel = MileageViewModelPreviewImpl())
    }
}

@Composable
fun MainComposable(
    viewModel: MileageViewModel = viewModel(
        modelClass = MileageViewModelImpl::class,
        factory = AppViewModelProvider.Factory
    ),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    CompositionLocalProvider(
        values = arrayOf(
            LocalSnackbarHostState provides snackBarHostState
        )
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackBarHostState) },
        ) { innerPadding ->
            MileageSliderPager(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

