package com.foodics.nour.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodics.core.util.connectivityObserver.ConnectivityObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    connectivityObserver: ConnectivityObserver
) : ViewModel() {

    val isConnected = connectivityObserver
        .isConnected
        .distinctUntilChanged()
        .drop(1)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            null
        )


}