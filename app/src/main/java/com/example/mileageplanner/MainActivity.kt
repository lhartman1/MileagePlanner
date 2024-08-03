package com.example.mileageplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mileageplanner.ui.MileageSliderPager
import com.example.mileageplanner.ui.theme.MileagePlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MileagePlannerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MileageSliderPager(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
