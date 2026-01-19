package com.foodics.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.foodics.core.ui.navigation.Route
import com.foodics.feature.menu.ui.navigation.MenuRoute
import com.foodics.feature.orders.ui.navigation.OrdersRoute
import com.foodics.feature.settingss.ui.navigation.SettingsRoute
import com.foodics.feature.tables.ui.navigation.TablesRoute

enum class BottomAppBarItem(
    val route: Route,
    @param:StringRes val title: Int,
    @param:DrawableRes val icon: Int,
    var badgeAmount: MutableState<Int> = mutableIntStateOf(0)
) {
    Tables(
        route = TablesRoute,
        title = R.string.tables,
        icon = R.drawable.ic_tables_bottom_bar
    ),
    Orders(
        route = OrdersRoute,
        title = R.string.orders,
        icon = R.drawable.ic_orders_bottom_bar
    ),
    Menu(
        route = MenuRoute,
        title = R.string.menu,
        icon = R.drawable.ic_menu_bottom_bar,
        badgeAmount = mutableIntStateOf(0)
    ),
    Settings(
        route = SettingsRoute,
        title = R.string.settings,
        icon = R.drawable.ic_settings_bottom_bar
    );

    companion object {
        fun updateBadgeAmount(route: Route, amount: Int) {
            entries.find { it.route == route }?.badgeAmount?.value = amount
        }

        fun updateBadgeAmount(routeString: String, amount: Int) {
            entries
                .firstOrNull { it.route.getRoute() == routeString }
                ?.badgeAmount
                ?.value = amount
        }

        fun getNavigationRoutes(): List<String> =
            entries.map { it.route.getRoute() }

        fun getNavigationItems(): List<BottomAppBarItem> =
            entries.toList()

        fun fromRoute(routeString: String?): BottomAppBarItem? =
            entries.firstOrNull { it.route.getRoute() == routeString }

        fun clearBadges() {
            entries.forEach { it.badgeAmount.value = 0 }
        }
    }
}