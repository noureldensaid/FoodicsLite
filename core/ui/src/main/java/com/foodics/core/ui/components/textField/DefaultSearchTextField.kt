package com.foodics.core.ui.components.textField

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.R
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.extensions.skipInteraction
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DefaultSearchTextField(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    query: String = "",
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    placeholder: String = stringResource(R.string.Search_for_product),
    debounceTime: Long = 500L,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            painter = painterResource(R.drawable.ic_search),
            contentDescription = "Search",
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
    },
    trailingIcon: @Composable ((onClear: () -> Unit) -> Unit)? = { onClear ->
        IconButton(
            onClick = onClear,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                painter = painterResource(R.drawable.ic_close_search),
                contentDescription = "Clear search",
                tint = iconTint,
            )
        }
    },
    searchBackground: Color = MaterialTheme.colorScheme.background,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unFocusedBorderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 12.dp,
    height: Dp = 40.dp,
    enabled: Boolean = true,
    onImeSearch: (() -> Unit)? = null,
) {
    var searchText by remember { mutableStateOf(query) }
    var isSearchFocused by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    val imeBottomPx = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeVisible = imeBottomPx > 0

    LaunchedEffect(imeVisible) {
        if (!imeVisible && isSearchFocused) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(query) { searchText = query }

    val debounceJob = remember { mutableStateOf<Job?>(null) }
    val latestOnSearch = rememberUpdatedState(onSearch)

    LaunchedEffect(searchText) {
        debounceJob.value?.cancel()
        debounceJob.value = coroutineScope.launch {
            delay(debounceTime)
            latestOnSearch.value(searchText)
        }
    }

    LaunchedEffect(imeVisible) {
        if (!imeVisible && isSearchFocused) {
            focusManager.clearFocus()
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                borderWidth,
                if (isSearchFocused) focusedBorderColor else unFocusedBorderColor,
                RoundedCornerShape(cornerRadius)
            )
            .background(searchBackground)
            .animateContentSize()
    ) {
        BasicTextField(
            value = searchText,
            enabled = enabled, interactionSource = skipInteraction(),
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { isSearchFocused = it.isFocused },
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = textColor,
                fontWeight = FontWeight.Normal
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onImeSearch?.invoke() ?: onSearch(searchText)
                }
            ),
            cursorBrush = SolidColor(cursorColor),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CompositionLocalProvider(LocalContentColor provides iconTint) {
                        leadingIcon?.invoke()
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = if (leadingIcon != null) 8.dp else 0.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            DefaultText(
                                text = placeholder,
                                fontSize = 13.sp,
                                fontColor = placeholderColor,
                                maxLines = 1,
                            )
                        }
                        innerTextField()
                    }

                    if (searchText.isNotEmpty() && trailingIcon != null) {
                        CompositionLocalProvider(LocalContentColor provides iconTint) {
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut() + scaleOut()
                            ) {
                                trailingIcon {
                                    searchText = ""
                                    onSearch("")
                                    focusRequester.requestFocus()
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun DefaultSearchTextFieldPreview() {
    DefaultSearchTextField(
        query = "Pis",
        onSearch = { searchQuery ->
        },
    )
}