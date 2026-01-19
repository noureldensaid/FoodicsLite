package com.foodics.feature.menu.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.menu.ui.screen.MenuScreen
import kotlinx.serialization.Serializable

@Serializable
data object MenuRoute : Route

fun NavGraphBuilder.menuNavGraph(
) {
    composable<MenuRoute> {
        MenuScreen()
    }
}