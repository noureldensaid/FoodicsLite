package com.foodics.core.ui.components.emptyStates

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.R
import com.foodics.core.ui.components.button.DefaultButton
import com.foodics.core.ui.components.text.DefaultText

@Composable
fun DefaultEmptyState(
    modifier: Modifier = Modifier,
    @StringRes body: Int? = R.string.no_products_was_found_details,
    @StringRes title: Int? = R.string.no_products_was_found,
    @StringRes buttonText: Int = R.string.try_again,
    @DrawableRes drawableResId: Int = R.drawable.ic_connection_error,
    action: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .padding(14.dp),
            painter = painterResource(id = drawableResId),
            contentDescription = null
        )

        title?.let {
            DefaultText(
                text = stringResource(id = it),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        body?.let {
            DefaultText(
                text = stringResource(id = it),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }

        action?.let {
            DefaultButton(
                modifier = Modifier.padding(30.dp),
                text = stringResource(id = buttonText),
                fontSize = 15.sp,
                onClick = action,
                enabled = true
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmptyState() {
    DefaultEmptyState(
        title = R.string.no_products_was_found,
        body = R.string.no_products_was_found_details,
        drawableResId = R.drawable.ic_connection_error,
    ) {}
}