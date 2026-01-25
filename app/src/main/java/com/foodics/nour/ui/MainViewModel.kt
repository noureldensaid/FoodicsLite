package com.foodics.nour.ui

import androidx.lifecycle.ViewModel
import com.foodics.core.util.connectivityObserver.ConnectivityObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.scan

class MainViewModel(
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val isConnectedState: Flow<Boolean> = connectivityObserver.isConnected
        .distinctUntilChanged()
        .scan(null as Boolean? to null as Boolean?) { result, current ->
            val previous = result.second
            previous to current
        }
        .drop(1)
        .mapNotNull { (previous, current) ->
            when {
                previous == null -> null
                previous && current?.not()!! -> false
                previous.not() && current == true -> true
                else -> null
            }
        }

}