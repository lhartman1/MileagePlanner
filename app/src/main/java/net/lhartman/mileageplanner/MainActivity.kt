package net.lhartman.mileageplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import net.lhartman.mileageplanner.ui.MainComposable
import net.lhartman.mileageplanner.ui.theme.MileagePlannerTheme

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
