package com.foodics.feature.tables.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.foodics.core.ui.R
import com.foodics.core.ui.extensions.skipInteraction
import com.foodics.tables.domain.model.Product

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        interactionSource = skipInteraction(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                model = product.image,
                error = painterResource(R.drawable.ic_image_placeholder),
                fallback = painterResource(R.drawable.ic_image_placeholder),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                text = product.name,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreviewLight() {
    MaterialTheme {
        ProductCard(
            modifier = Modifier.size(150.dp),
            product = Product(
                id = "p1",
                name = "Product Name",
                description = "Product Description",
                image = "core/ui/src/main/res/drawable/ic_image_placeholder.xml",
                price = 123.0,
                quantity = 0,
                categoryId = "1",
                categoryName = "Category Name"
            ),
            onClick = {}
        )
    }
}