package com.foodics.core.navigation

import androidx.navigation.NavHostController
import com.foodics.core.ui.navigation.Route

fun NavHostController.navigateWithPopUpTo(screen: Any, popUp: Any) {
    navigate(screen) {
        popUpTo(popUp) { inclusive = true }
    }
}

fun Route.getRoute(): String {
    return this::class.java.canonicalName.orEmpty()
}