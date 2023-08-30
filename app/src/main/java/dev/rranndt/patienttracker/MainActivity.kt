package dev.rranndt.patienttracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.rranndt.patienttracker.navigation.NavGraphSetup
import dev.rranndt.patienttracker.presentation.theme.PatientTrackerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PatientTrackerTheme {
                val navController = rememberNavController()
                NavGraphSetup(navController = navController)
            }
        }
    }
}