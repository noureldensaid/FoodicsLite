package com.foodics.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.foodics.core.common.result.ResponseState
import kotlinx.coroutines.flow.Flow

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    isLoading: (show: Boolean) -> Unit,
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit,
    onRetry: (() -> Unit) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = MainFlow,
        modifier = modifier,
    ) {
        mainFlowNavigation(
            navController = navController,
            isLoading = isLoading,
            errorFlow = errorFlow,
            onRetry = onRetry
        )
    }
}