package com.foodics.feature.tables.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.foodics.core.common.util.ObserveAsEvents
import com.foodics.feature.tables.ui.viewmodel.TablesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TablesScreen() {

    val viewModel : TablesViewModel = koinViewModel()
    val state by viewModel.state.collectAsState()
    val error = viewModel.errorFlow

    ObserveAsEvents(error){ error ->
        Log.d("TablesViewModel", "error: $error")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = state.toString())
    }
}