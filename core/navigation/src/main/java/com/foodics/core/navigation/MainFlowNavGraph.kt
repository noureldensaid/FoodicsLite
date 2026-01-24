package com.foodics.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigation
import com.foodics.core.common.result.ResponseState
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.menu.ui.navigation.menuNavGraph
import com.foodics.feature.orders.ui.navigation.ordersNavGraph
import com.foodics.feature.settingss.ui.navigation.settingsNavGraph
import com.foodics.feature.tables.ui.navigation.TablesRoute
import com.foodics.feature.tables.ui.navigation.tablesNavGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
object MainFlow : Route

internal fun NavGraphBuilder.mainFlowNavigation(
    navController: NavHostController,
    isLoading: (show: Boolean) -> Unit,
    errorFlow: (error: Flow<ResponseState.Error>) -> Unit,
) {
    navigation<MainFlow>(TablesRoute) {

        tablesNavGraph(
            navController = navController,
            isLoading = isLoading,
            errorFlow = errorFlow,
        )

        menuNavGraph()
        settingsNavGraph()
        ordersNavGraph()
    }
}