package com.foodics.feature.settingss.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.settingss.ui.screen.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute : Route

fun NavGraphBuilder.settingsNavGraph(
) {
    composable<SettingsRoute> {
        SettingsScreen()
    }
}