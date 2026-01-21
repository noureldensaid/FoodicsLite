package com.foodics.core.ui.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.extensions.onClick
import com.foodics.core.ui.theme.AppTypography
import com.foodics.core.ui.theme.FoodicsLiteTheme

@Composable
fun DefaultText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    onClick: (() -> Unit)? = null,
    fontColor: Color = MaterialTheme.colorScheme.onBackground,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = TextStyle(
        lineHeight = 16.sp,
        fontWeight = fontWeight,
        fontFamily = AppTypography.bodyMedium.fontFamily,
        platformStyle = PlatformTextStyle(includeFontPadding = true),
    )
) {
    val content = @Composable {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
            color = fontColor,
            textAlign = textAlign,
            modifier = if (onClick != null) modifier.onClick { onClick() } else modifier,
            style = style,
            maxLines = maxLines,
            minLines = minLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = onTextLayout ?: {},
        )
    }
    content()
}

@Preview(showBackground = true)
@Composable
private fun DefaultTextPreview() {
    FoodicsLiteTheme {
        DefaultText(text = "Hello World!")
    }
}
