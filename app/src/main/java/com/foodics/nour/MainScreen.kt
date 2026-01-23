package com.foodics.nour

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.util.ObserveAsEvents
import com.foodics.core.common.util.SnackbarAction
import com.foodics.core.common.util.SnackbarController
import com.foodics.core.navigation.AppNavGraph
import com.foodics.core.navigation.BottomAppBar
import com.foodics.core.navigation.BottomAppBarItem
import com.foodics.core.network.NetworkError
import com.foodics.core.ui.components.loading.DefaultLoadingComponent
import com.foodics.core.ui.components.popUps.DefaultConnectionError
import com.foodics.core.ui.components.snackBar.DefaultSnackbar
import com.foodics.core.ui.theme.FoodicsLiteTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    var errorFlow by remember { mutableStateOf(flowOf<ResponseState.Error>()) }

    var onRetry: () -> Unit by remember { mutableStateOf({}) }

    var isNetworkConnectionError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val navController = rememberNavController()

    val visibleBottomSheetScreen = BottomAppBarItem.getNavigationRoutes()

    val bottomBarVisibility =
        navController.currentBackStackEntryAsState().value?.destination?.route in visibleBottomSheetScreen

    ObserveAsEvents(flow = errorFlow) { error ->
        when (error.error) {
            NetworkError.NO_INTERNET_CONNECTION -> isNetworkConnectionError = true
            else -> scope.launch {
                SnackbarController.sendEvent(
                    event = SnackbarAction.SendEvent(
                        name = error.errorBody?.message ?: error.error.toString(),
                        label = context.getString(R.string.ok)
                    )
                )
            }
        }
    }

    ObserveAsEvents(
        flow = SnackbarController.events,
        snackbarHostState
    ) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            when (event) {
                is SnackbarAction.Dismiss -> snackbarHostState.currentSnackbarData?.dismiss()
                is SnackbarAction.SendEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.name,
                        actionLabel = event.label,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    FoodicsLiteTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier.padding(16.dp),
                    hostState = snackbarHostState
                ) {
                    DefaultSnackbar(
                        message = it.visuals.message,
                        actionText = it.visuals.actionLabel
                    ) {
                        scope.launch {
                            SnackbarController.sendEvent(
                                event = SnackbarAction.Dismiss
                            )
                        }
                    }
                }
            },
            content = { _ ->
                Box(Modifier.fillMaxSize()) {
                    AppNavGraph(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        isLoading = { isLoading = it },
                        errorFlow = { errorFlow = it },
                        onRetry = { onRetry = { it.invoke() } },
                    )
                    DefaultConnectionError(
                        isVisible = isNetworkConnectionError,
                    ) {
                        onRetry()
                        isNetworkConnectionError = false
                    }

                    DefaultLoadingComponent(isLoading)
                }
            },
            bottomBar = {
                AnimatedVisibility(bottomBarVisibility) {
                    BottomAppBar(
                        navController = navController,
                    )
                }
            }
        )
    }
}