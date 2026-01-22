package com.foodics.feature.tables.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.components.icons.BackIcon
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.theme.FoodicsLiteTheme
import com.foodics.core.ui.theme.green
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablesTopBar(
    modifier: Modifier = Modifier,
    isSyncing: Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                DefaultText(
                    text = "Nour Elden Said",
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                DefaultText(
                    text = Random.nextInt(1, 999).toString(),
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .background(green)
                )
            }
        }
        TopAppBar(
            windowInsets = WindowInsets(top = 0),
            title = {
                DefaultText(
                    text = "Menu",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            },
            navigationIcon = {
                BackIcon {}
            },
            actions = {
                // simple “header” actions like screenshot
                Icon(
                    Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                DefaultText(Random.nextInt(1, 99).toString() , style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.width(18.dp))
                Icon(
                    Icons.Default.PeopleAlt,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
                DefaultText(Random.nextInt(1, 99).toString(), style = MaterialTheme.typography.titleSmall)
            }
        )

        if (isSyncing) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TablesTopBarPreview() {
    FoodicsLiteTheme {
        TablesTopBar(isSyncing = false)
    }
}

@Preview(showBackground = true, name = "Syncing")
@Composable
fun TablesTopBarSyncingPreview() {
    FoodicsLiteTheme {
        TablesTopBar(isSyncing = true)
    }
}
