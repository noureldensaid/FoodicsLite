package com.foodics.core.ui.components.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.components.text.DefaultText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isInInFocus: ((Boolean) -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    isError: Boolean = false,
    isEnabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = TextFieldDefaults.colors().copy(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f),
        errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = TextStyle(
        fontSize = 12.sp,
        color = MaterialTheme.colorScheme.onSurface
    ),
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val focusState = remember { mutableStateOf(false) }

    LaunchedEffect(focusState.value) {
        isInInFocus?.invoke(focusState.value)
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState.value = it.isFocused }
            .clip(shape)
            .height(44.dp)
            .background(color = backgroundColor)
            .border(
                color = when {
                    isError -> MaterialTheme.colorScheme.error
                    focusState.value -> focusedBorderColor
                    else -> MaterialTheme.colorScheme.outline
                },
                shape = shape,
                width = 1.dp
            ),
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        enabled = isEnabled,
        singleLine = singleLine,
        keyboardActions = keyboardActions,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = singleLine,
            enabled = isEnabled,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 13.dp),
            label = label,
            trailingIcon = trailingIcon,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            colors = colors,
        )
    }
}

@Preview
@Composable
private fun DefaultTextFieldPreview() {
    DefaultTextField(
        value = "Dummy Text",
        onValueChange = {},
        label = { DefaultText(text = "Label") },
    )
}