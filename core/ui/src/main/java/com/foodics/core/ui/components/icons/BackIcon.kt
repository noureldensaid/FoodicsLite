package com.foodics.core.ui.components.icons

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.foodics.core.ui.extensions.onClick

@Composable
fun BackIcon(modifier: Modifier = Modifier, iconTint: Color = MaterialTheme.colorScheme.onBackground, onClick: () -> Unit) {

    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Icon(
        Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = null,
        modifier = modifier
            .size(24.dp)
            .onClick {
                onBackPressedDispatcher?.onBackPressed()
                onClick()
            },
        tint = iconTint
    )
}