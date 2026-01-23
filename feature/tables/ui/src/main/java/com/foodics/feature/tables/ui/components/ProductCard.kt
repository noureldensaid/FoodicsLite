package com.foodics.feature.tables.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.foodics.core.ui.R
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.extensions.skipInteraction
import com.foodics.core.ui.theme.red
import com.foodics.tables.domain.model.Product

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: () -> Unit
) {
    BadgedBox(
        badge = {
            AnimatedVisibility(product.quantity > 0) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(red),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        modifier = Modifier.offset(y = (-2).dp),
                        text = product.quantity.toString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1
                    )
                }
            }
        },
    ) {
        ElevatedCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.5.dp),
            shape = RoundedCornerShape(6.dp),
            interactionSource = skipInteraction(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
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
                DefaultText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    text = product.name,
                    maxLines = 2,
                    minLines = 2,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductCardPreview() {
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