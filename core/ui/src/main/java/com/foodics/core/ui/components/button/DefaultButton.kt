package com.foodics.core.ui.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.extensions.skipInteraction

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    text: String,
    height: Dp = 40.dp,
    shape: Shape = RoundedCornerShape(8.dp),
    color: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    padding: PaddingValues = PaddingValues(0.dp),
    widthPercentage: Float = 1f,
    fontSize: TextUnit = 15.sp,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .height(height)
            .fillMaxWidth(widthPercentage),
        shape = shape,
        contentPadding = padding,
        onClick = onClick,
        colors = if (enabled)
            ButtonDefaults.buttonColors(containerColor = color)
        else
            ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
        enabled = enabled,
        interactionSource = skipInteraction()
    ) {
        DefaultText(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = text,
            fontColor = if (enabled) textColor else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium,
            fontSize = fontSize
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultButtonPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            DefaultButton(
                text = "Enabled Button",
                onClick = {}
            )
            Spacer(modifier = Modifier.height(12.dp))
            DefaultButton(
                text = "Disabled Button",
                enabled = false,
                onClick = {}
            )
        }
    }
}