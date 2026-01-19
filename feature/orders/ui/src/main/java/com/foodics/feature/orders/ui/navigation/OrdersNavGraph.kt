package com.foodics.feature.orders.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.orders.ui.screen.OrdersScreen
import kotlinx.serialization.Serializable

@Serializable
data object OrdersRoute : Route

fun NavGraphBuilder.ordersNavGraph(
) {
    composable<OrdersRoute> {
        OrdersScreen()
    }
}