package com.foodics.nour.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.foodics.core.ui.theme.FoodicsLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        setContent {
            FoodicsLiteTheme {
            MainScreen()
            }
        }
    }
}
