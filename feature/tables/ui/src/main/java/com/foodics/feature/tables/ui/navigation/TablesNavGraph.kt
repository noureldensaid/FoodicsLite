package com.foodics.feature.tables.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.tables.ui.screen.TablesScreen
import kotlinx.serialization.Serializable

@Serializable
data object TablesRoute : Route

fun NavGraphBuilder.tablesNavGraph(
) {
    composable<TablesRoute> {
        TablesScreen()
    }
}