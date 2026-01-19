package com.foodics.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

interface Navigator {

    val navigationEvents: Channel<NavigationEvent>

    fun navigate(
        route: Any,
        popUpToRoute: Any? = null,
        inclusive: Boolean = false,
        isSingleTop: Boolean = false,
    )

    fun popBackStack(route: Any? = null, inclusive: Boolean = false)
}

sealed class NavigationEvent {

    data class Navigate(
        val route: Any,
        val popUpToRoute: Any? = null,
        val inclusive: Boolean = false,
        val isSingleTop: Boolean = false,
    ) : NavigationEvent()

    data class PopBackStack(val route: Any? = null, val inclusive: Boolean = false) :
        NavigationEvent()
}

class NavigatorImpl : Navigator {

    private val scope = CoroutineScope(Dispatchers.Main)

    override val navigationEvents: Channel<NavigationEvent> = Channel(Channel.UNLIMITED)

    override fun navigate(
        route: Any,
        popUpToRoute: Any?,
        inclusive: Boolean,
        isSingleTop: Boolean,
    ) {
        scope.launch {
            navigationEvents.send(
                NavigationEvent.Navigate(
                    route,
                    popUpToRoute,
                    inclusive,
                    isSingleTop,
                )
            )
        }
    }

    override fun popBackStack(route: Any?, inclusive: Boolean) {
        scope.launch {
            navigationEvents.send(NavigationEvent.PopBackStack(route, inclusive))
        }
    }
}