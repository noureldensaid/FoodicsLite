package com.foodics.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodics.core.ui.extensions.conditional
import com.foodics.core.ui.extensions.noRippleClickable

@Composable
fun BottomAppBar(
    navController: NavController,
) {
    val tabList = BottomAppBarItem.getNavigationItems()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = Color.White,
    ) {
        tabList.forEach { navItem ->
            val isSelected = currentRoute == navItem.route.getRoute()
            key(navItem.route.getRoute()) {
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(if (isSelected) 1f else 0.5f)
                        .background(
                            color = if (isSelected) Color.Blue else Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .conditional(isSelected) { padding(top = 8.dp, bottom = 8.dp) }
                        .noRippleClickable {
                            navController.navigate(navItem.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(
                            horizontal = if (isSelected) 20.dp else 0.dp,
                            vertical = 12.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(if (isSelected) 16.dp else 24.dp),
                        imageVector = ImageVector.vectorResource(navItem.icon),
                        tint = if (isSelected) Color.Blue else Color.Gray,
                        contentDescription = null,
                    )
                    AnimatedVisibility(isSelected) {
                        Text(
                            text = stringResource(navItem.title),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizAppBottomBarPreview() {
    BottomAppBar(
        navController = rememberNavController(),
    )
}