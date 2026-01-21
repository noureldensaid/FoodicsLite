package com.foodics.feature.tables.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.foodics.feature.tables.ui.model.TablesScreenEvent
import com.foodics.feature.tables.ui.model.TablesScreenState

@Composable
fun TableScreenRoot(
    modifier: Modifier = Modifier,
    state: TablesScreenState,
    onEvent: (TablesScreenEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = state.toString())
    }
}