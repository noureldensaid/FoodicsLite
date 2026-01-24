package com.foodics.core.util.connectivityObserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean?>
}