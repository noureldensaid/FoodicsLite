package com.foodics.core.ui.components.loading

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun DefaultLoadingComponent(
    isVisible: Boolean,
) {
    if (isVisible) {
        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
            ),
            onDismissRequest = {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = ProgressIndicatorDefaults.circularColor,
                strokeWidth = 10.dp,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun LoadingComponentPreview() {
    DefaultLoadingComponent(true)
}