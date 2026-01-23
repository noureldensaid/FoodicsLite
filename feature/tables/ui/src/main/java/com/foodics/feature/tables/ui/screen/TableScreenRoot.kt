package com.foodics.feature.tables.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodics.core.ui.components.emptyStates.DefaultEmptyState
import com.foodics.core.ui.components.textField.DefaultSearchTextField
import com.foodics.core.ui.theme.FoodicsLiteTheme
import com.foodics.feature.tables.ui.components.CategoryChip
import com.foodics.feature.tables.ui.components.ProductCard
import com.foodics.feature.tables.ui.components.ProductCardLoadingShimmer
import com.foodics.feature.tables.ui.components.TablesTopBar
import com.foodics.feature.tables.ui.components.ViewOrderBar
import com.foodics.feature.tables.ui.model.TablesScreenEvent
import com.foodics.feature.tables.ui.model.TablesScreenState
import com.foodics.tables.domain.model.CartSummary
import kotlinx.coroutines.launch

@Composable
fun TableScreenRoot(
    modifier: Modifier = Modifier,
    state: TablesScreenState,
    onEvent: (TablesScreenEvent) -> Unit
) {

    val scope = rememberCoroutineScope()

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()

    val lazyRowState = rememberLazyListState()

    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { state.categories.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
        if (state.categories.isNotEmpty()) {
            onEvent(TablesScreenEvent.OnCategorySelected(state.categories[selectedTabIndex].id))
            pagerState.scrollToPage(selectedTabIndex)
            lazyRowState.animateScrollToItem(selectedTabIndex)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TablesTopBar(isSyncing = state.isSyncing) },
        bottomBar = {
            AnimatedVisibility(visible = state.cartSummary.totalQty > 0) {
                ViewOrderBar(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp,
                        bottom = 50.dp + NavigationBarDefaults.windowInsets.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                    qty = state.cartSummary.totalQty,
                    totalPrice = state.cartSummary.totalPrice,
                    onClick = { onEvent(TablesScreenEvent.OnViewOrderClicked) }
                )
            }
        }
    ) { padding ->
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.background,
                    color = MaterialTheme.colorScheme.primary,
                    state = pullToRefreshState
                )
            },
            onRefresh = {
                isRefreshing = true
                onEvent(TablesScreenEvent.LoadInitialData)
                isRefreshing = false
            },
        ) {
            Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
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
                state = lazyRowState,
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.Top
            ) {
                items(state.categories, key = { it.id }) { cat ->
                    CategoryChip(
                        title = cat.name,
                        selected = cat.id == state.selectedCategoryId,
                        onClick = {
                            selectedTabIndex = state.categories.indexOf(cat)
                            scope.launch {
                                pagerState.scrollToPage(selectedTabIndex)
                                lazyRowState.animateScrollToItem(selectedTabIndex)
                            }
                            onEvent(TablesScreenEvent.OnCategorySelected(cat.id))
                        }
                    )
                }
            }

            if (state.categories.isNotEmpty()) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    userScrollEnabled = state.searchQuery.isBlank(),
                ) {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterVertically
                        ),
                        columns = GridCells.Adaptive(100.dp),
                        contentPadding = PaddingValues(
                            top = 12.dp,
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding() + 48.dp
                        )
                    ) {
                        when {
                            state.isLoading -> {
                                items(10) {
                                    ProductCardLoadingShimmer(modifier = Modifier.size(150.dp))
                                }
                            }

                            state.isLoading.not() && state.products.isEmpty() -> {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    DefaultEmptyState()
                                }
                            }

                            else -> {
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
                }
            }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TablesScreenPreview_WithCartBar() {
    FoodicsLiteTheme {
        TableScreenRoot(
            state = TablesScreenState(
                isLoading = false,
                isSyncing = false,
                showOrderPreview = true,
                cartSummary = CartSummary(
                    totalQty = 5,
                    totalPrice = 1234.0
                )
            ),
            onEvent = {}
        )
    }
}