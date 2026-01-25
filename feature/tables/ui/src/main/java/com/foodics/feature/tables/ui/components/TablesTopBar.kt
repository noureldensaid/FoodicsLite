package com.foodics.feature.tables.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foodics.core.ui.components.icons.BackIcon
import com.foodics.core.ui.components.text.DefaultText
import com.foodics.core.ui.theme.FoodicsLiteTheme
import com.foodics.core.ui.theme.green
import com.foodics.core.ui.theme.red
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablesTopBar(
    modifier: Modifier = Modifier,
    isSyncing: Boolean,
    isOnline: Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                ),
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
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
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
                    text = Random.nextInt(1, 999).toString().padStart(2, '0'),
                )
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(16.dp)
                        .background(if (isOnline) green else red)
                )
            }
        }
        AnimatedVisibility (
            visible = isSyncing
        ) {
            LinearProgressIndicator(
                modifier=Modifier.fillMaxWidth(),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                color = MaterialTheme.colorScheme.primary
            )
        }
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            windowInsets = WindowInsets(top = 0, bottom = 0),
            title = {
                DefaultText(
                    text = "Menu",
                    fontWeight = Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                BackIcon {}
            },
            actions = {
                // simple “header” actions like screenshot
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp),
                    contentDescription = null,
                )
                Spacer(Modifier.width(6.dp))
                DefaultText(
                    text = Random.nextInt(1, 99).toString().padStart(2, '0'),
                    fontSize = 14.sp
                )
                Spacer(Modifier.width(18.dp))
                Icon(
                    imageVector = Icons.Default.PeopleAlt,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(20.dp),
                    contentDescription = null,
                )
                Spacer(Modifier.width(6.dp))
                DefaultText(
                    text = Random.nextInt(1, 99).toString().padStart(2, '0'),
                    fontSize = 14.sp
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TablesTopBarPreview() {
    FoodicsLiteTheme {
        TablesTopBar(isSyncing = false, isOnline = false)
    }
}

@Preview(showBackground = true, name = "Syncing")
@Composable
fun TablesTopBarSyncingPreview() {
    FoodicsLiteTheme {
        TablesTopBar(isSyncing = true, isOnline = true)
    }
}
