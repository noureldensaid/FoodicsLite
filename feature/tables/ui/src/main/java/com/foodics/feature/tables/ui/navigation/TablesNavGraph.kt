package com.foodics.feature.tables.ui.navigation

import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.foodics.core.common.result.ResponseState
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.tables.ui.screen.TablesScreen
import com.foodics.feature.tables.ui.viewmodel.TablesViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data object TablesRoute : Route

fun NavGraphBuilder.tablesNavGraph(
    navController: NavHostController,
    isLoading: (show: Boolean) -> Unit,
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit,
) {
    composable<TablesRoute> { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(navController.graph.id)
        }
        val viewModel: TablesViewModel = koinViewModel(viewModelStoreOwner = parentEntry)

        TablesScreen(
            viewModel = viewModel,
            isLoading = isLoading,
            errorFlow = errorFlow,
        )
    }
}