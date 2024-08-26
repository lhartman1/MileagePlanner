package net.lhartman.mileageplanner.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import net.lhartman.mileageplanner.ui.theme.MileagePlannerTheme

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
    ComposeLocalWrapper {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(LocalSnackbarHostState.current) },
        ) { innerPadding ->
            MileageSliderPager(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

