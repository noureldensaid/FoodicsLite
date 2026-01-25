package com.foodics.core.ui.components.popUps

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.foodics.core.ui.R
import com.foodics.core.ui.components.button.DefaultButton
import com.foodics.core.ui.components.text.DefaultText

@Composable
fun DefaultConnectionError(
    isVisible: Boolean = false,
    @StringRes title: Int = R.string.no_internet,
    @StringRes body: Int = R.string.no_internet_details,
    @StringRes buttonText: Int = R.string.try_again,
    painter: Painter = painterResource(id = R.drawable.ic_connection_error),
    onRetry: () -> Unit = {},
) {
    if (!isVisible) return

    // Full overlay within Scaffold's body area only
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
            .background(MaterialTheme.colorScheme.background)
            // consume all input so taps don't leak to underlying content
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Image(
                painter = painter,
                contentDescription = null
            )

            DefaultText(
                text = stringResource(id = title),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )

            DefaultText(
                text = stringResource(id = body),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            DefaultButton(
                text = stringResource(id = buttonText),
                onClick = onRetry
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConnectionError() {
    DefaultConnectionError(
        isVisible = true,
        onRetry = {}
    )
}