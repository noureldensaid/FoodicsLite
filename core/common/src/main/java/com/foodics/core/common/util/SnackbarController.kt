package com.foodics.core.common.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarController {

    private val _events = Channel<SnackbarAction>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: SnackbarAction) {
        _events.send(event)
    }
}

sealed class SnackbarAction {
    data class SendEvent(
        val name: String,
        val label: String? = null,
        val action: suspend () -> Unit = {}
    ) : SnackbarAction()

    data object Dismiss : SnackbarAction()
}