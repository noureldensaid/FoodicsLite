package com.foodics.feature.tables.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodics.core.ui.components.textField.DefaultSearchTextField
import com.foodics.feature.tables.ui.components.CategoryChip
import com.foodics.feature.tables.ui.components.ProductCard
import com.foodics.feature.tables.ui.components.TablesTopBar
import com.foodics.feature.tables.ui.components.ViewOrderBar
import com.foodics.feature.tables.ui.model.TablesScreenEvent
import com.foodics.feature.tables.ui.model.TablesScreenState
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.model.Product
import java.util.Locale

@Composable
fun TableScreenRoot(
    modifier: Modifier = Modifier,
    state: TablesScreenState,
    onEvent: (TablesScreenEvent) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TablesTopBar(
                modifier = Modifier.padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp
                ),
                isSyncing = state.isSyncing
            )
        },
        bottomBar = {
            AnimatedVisibility(visible = state.cartSummary.totalQty > 0) {
                ViewOrderBar(
                    qty = state.cartSummary.totalQty,
                    totalPrice = state.cartSummary.totalPrice,
                    onClick = { onEvent(TablesScreenEvent.OnViewOrderClicked) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DefaultSearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                query = state.searchQuery,
                onSearch = { searchQuery ->
                    onEvent(TablesScreenEvent.OnSearchQueryChanged(searchQuery))
                }
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(state.categories, key = { it.id }) { cat ->
                    CategoryChip(
                        title = cat.name,
                        selected = cat.id == state.selectedCategoryId,
                        onClick = { onEvent(TablesScreenEvent.OnCategorySelected(cat.id)) }
                    )
                }
            }
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                columns = GridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(
                    bottom = WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding() + 48.dp
                )
            ) {
                items(
                    items = state.products,
                    key = { it.id }
                ) { product ->
                    ProductCard(
                        product = product,
                        onClick = {
                            onEvent(TablesScreenEvent.OnProductClicked(product.id))
                        }
                    )
                }
            }
        }
    }

    if (state.showOrderPreview) {
        OrderPreviewBottomSheet(
            items = state.orderPreviewItems,
            totalQty = state.orderPreviewSummary.totalQty,
            totalPrice = state.orderPreviewSummary.totalPrice,
            onDismiss = { onEvent(TablesScreenEvent.OnDismissOrderPreview) }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderPreviewBottomSheet(
    items: List<Product>,
    totalQty: Int,
    totalPrice: Double,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Order preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(10.dp))

            if (items.isEmpty()) {
                Text("No items.")
            } else {
                items.take(50).forEach { p ->
                    Text(
                        text = "• ${p.name}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(6.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Qty", fontWeight = FontWeight.SemiBold)
                Text(totalQty.toString(), fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", fontWeight = FontWeight.SemiBold)
                Text(
                    "SAR ${"%.2f".format(Locale.US, totalPrice)}",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(18.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text("Close")
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TablesScreenPreview_Loading() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                isLoading = true,
                isSyncing = true
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TablesScreenPreview_Empty() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                isLoading = false,
                isSyncing = false,
                searchQuery = "",
                selectedCategoryId = null
                // categories/products empty by default
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TablesScreenPreview_WithCartBar() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                isLoading = false,
                isSyncing = false,
                cartSummary = CartSummary(
                    totalQty = 5,
                    totalPrice = 1234.0
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TablesScreenPreview_OrderPreviewVisible() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                isLoading = false,
                showOrderPreview = true,
                cartSummary = CartSummary(
                    totalQty = 5,
                    totalPrice = 1234.0
                ),
                orderPreviewSummary = CartSummary(
                    totalQty = 5,
                    totalPrice = 1234.0
                )
                // orderPreviewItems empty by default (safe)
            ),
            onEvent = {}
        )
    }
}