package com.example.mileageplanner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mileageplanner.ui.theme.MileagePlannerTheme

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
    )
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        MileageSliderPager(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

