package com.foodics.core.ui.components.snackBar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.R
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.extensions.onClick

@Composable
fun DefaultSnackbar(
    modifier: Modifier = Modifier,
    message: String,
    backgroundColor: Color = MaterialTheme.colorScheme.errorContainer,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    actionText: String? = stringResource(R.string.ok),
    shape: Shape = RoundedCornerShape(8.dp),
    padding: PaddingValues = PaddingValues(12.dp),
    textStyle: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
    contentAlignment: Alignment = Alignment.CenterStart,
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = shape)
            .defaultMinSize(minHeight = 50.dp)
            .padding(padding),
        contentAlignment = contentAlignment
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onClick { onDismiss() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = null,
                    tint = textColor
                )
                DefaultText(text = message, color = textColor, style = textStyle)

            }
            actionText?.let {
                DefaultText(
                    text = it,
                    modifier = Modifier.padding(start = 6.dp),
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultSnackbarPreview() {
    DefaultSnackbar(
        message = "This is a snackbar",
        onDismiss = {}
    )
}