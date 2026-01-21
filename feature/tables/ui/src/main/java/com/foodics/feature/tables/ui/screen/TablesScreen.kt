package com.foodics.feature.tables.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.foodics.core.common.result.ResponseState
import com.foodics.feature.tables.ui.model.TablesScreenEvent
import com.foodics.feature.tables.ui.viewmodel.TablesViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun TablesScreen(
    viewModel: TablesViewModel,
    isLoading: (show: Boolean) -> Unit = {},
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit = {},
    onRetry: (() -> Unit) -> Unit = {}
) {

    val state by viewModel.state.collectAsState()

    isLoading(state.isLoading)

    onRetry {
        viewModel.onEvent(TablesScreenEvent.LoadInitialData)
    }

    errorFlow(viewModel.errorFlow)

    TableScreenRoot(
        state = state,
        onEvent = viewModel::onEvent
    )
}