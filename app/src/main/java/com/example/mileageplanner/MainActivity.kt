package com.example.mileageplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mileageplanner.ui.MainComposable
import com.example.mileageplanner.ui.theme.MileagePlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MileagePlannerTheme {
                MainComposable()
            }
        }
    }
}
