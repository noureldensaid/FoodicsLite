package com.foodics.nour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.foodics.core.ui.theme.FoodicsLiteTheme
import com.foodics.navigation.AppNavGraph
import com.foodics.navigation.BottomAppBar
import com.foodics.navigation.BottomAppBarItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val visibleBottomSheetScreen = BottomAppBarItem.getNavigationRoutes()

            val bottomBarVisibility =
                navController.currentBackStackEntryAsState().value?.destination?.route in visibleBottomSheetScreen

            FoodicsLiteTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    content = { innerPadding ->
                        AppNavGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            navController = navController,
                        )
                    },
                    bottomBar = {
                        AnimatedVisibility(bottomBarVisibility) {
                            BottomAppBar(
                                navController = navController,
                            )
                        }
                    }
                )
            }
        }
    }
}