package dev.rranndt.patienttracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import dev.rranndt.patienttracker.presentation.patient_details.PatientDetailsScreen
import dev.rranndt.patienttracker.presentation.patient_list.PatientListScreen
import dev.rranndt.patienttracker.util.Constants.PATIENT_DETAILS_ARG_KEY

@Composable
fun NavGraphSetup(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PatientList.route
    ) {
        composable(route = Screen.PatientList.route) {
            PatientListScreen(
                onFabClicked = { navController.navigate(Screen.PatientDetails.route) },
                onItemClicked = { navController.navigate(Screen.PatientDetails.passPatientId(it)) }
            )
        }
        composable(
            route = Screen.PatientDetails.route,
            arguments = listOf(navArgument(name = PATIENT_DETAILS_ARG_KEY) {
                type = NavType.IntType
                defaultValue = -1
            })
        ) {
            PatientDetailsScreen(
                onBackClicked = { navController.navigateUp() },
                onSuccessfullySaving = { navController.navigateUp() }
            )
        }
    }
}