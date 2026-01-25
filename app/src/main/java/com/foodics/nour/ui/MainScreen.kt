package com.foodics.nour.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodics.core.common.error.NetworkError
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.util.ObserveAsEvents
import com.foodics.core.common.util.SnackbarAction
import com.foodics.core.common.util.SnackbarController
import com.foodics.core.navigation.AppNavGraph
import com.foodics.core.navigation.BottomAppBar
import com.foodics.core.navigation.BottomAppBarItem
import com.foodics.core.ui.components.loading.DefaultLoadingComponent
import com.foodics.core.ui.components.snackBar.DefaultSnackbar
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.theme.FoodicsLiteTheme
import com.foodics.core.ui.theme.amber
import com.foodics.core.ui.theme.green
import com.foodics.nour.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen() {

    val mainViewModel: MainViewModel = koinViewModel()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var isOfflineMode: Boolean? by rememberSaveable { mutableStateOf(null) }

    var isLoading by rememberSaveable { mutableStateOf(false) }

    var errorFlow by remember { mutableStateOf(flowOf<ResponseState.Error>()) }

    val snackbarHostState = remember { SnackbarHostState() }

    val navController = rememberNavController()

    val visibleBottomSheetScreen = BottomAppBarItem.getNavigationRoutes()

    val bottomBarVisibility =
        navController.currentBackStackEntryAsState().value?.destination?.route in visibleBottomSheetScreen

    val bgColor = when (isOfflineMode) {
        true -> amber
        false -> green
        else -> MaterialTheme.colorScheme.background
    }

    ObserveAsEvents(mainViewModel.isConnectedState) { isConnected ->
        isConnected?.let {
            isOfflineMode = !isConnected
            scope.launch {
                delay(2.seconds)
                isOfflineMode = null
            }
        }
    }

    ObserveAsEvents(flow = errorFlow) { error ->
        val errorMessage = if (error.error == NetworkError.NO_INTERNET_CONNECTION) context.getString(R.string.you_re_offline) else error.errorBody?.message
        when (error.error) {
            else -> scope.launch {
                SnackbarController.sendEvent(
                    event = SnackbarAction.SendEvent(
                        name = errorMessage ?: error.error.toString(),
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
                        duration = SnackbarDuration.Short
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
                    )
                    DefaultLoadingComponent(isLoading)
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgColor)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    AnimatedVisibility(
                        visible = bottomBarVisibility,
                        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    ) {
                        BottomAppBar(navController = navController)
                    }
                    AnimatedVisibility(
                        visible = isOfflineMode != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        isOfflineMode?.let {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                DefaultText(
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    text = if (isOfflineMode!!) stringResource(R.string.you_re_offline) else stringResource(
                                        R.string.you_re_back_online
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}