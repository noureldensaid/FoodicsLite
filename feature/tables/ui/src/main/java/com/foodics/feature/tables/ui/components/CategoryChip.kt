package com.foodics.feature.tables.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.extensions.onClick

@Composable
fun CategoryChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    var textWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val underlineWidthDp = with(density) { textWidthPx.toDp() }

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .onClick(onClick)
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DefaultText(
            text = title,
            fontColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            onTextLayout = { textWidthPx = it.size.width }
        )

        Box(
            modifier = Modifier
                .width(underlineWidthDp)
                .height(4.dp)
                .background(
                    color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    shape = RoundedCornerShape(100)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipPreview_Selected() {
    CategoryChip(
        title = "Category Name",
        selected = true,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryChipPreview_UnSelected() {
    CategoryChip(
        title = "Category Name",
        selected = false,
        onClick = {}
    )
}