package com.foodics.feature.tables.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.foodics.core.common.result.ResponseState
import com.foodics.feature.tables.ui.viewmodel.TablesViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun TablesScreen(
    viewModel: TablesViewModel,
    isLoading: (show: Boolean) -> Unit = {},
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit = {},
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    errorFlow(viewModel.errorFlow)

    TableScreenRoot(
        state = state,
        onEvent = viewModel::onEvent
    )
}