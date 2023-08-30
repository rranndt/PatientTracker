package dev.rranndt.patienttracker.navigation

import dev.rranndt.patienttracker.util.Constants.PATIENT_DETAILS_ARG_KEY

sealed class Screen(val route: String) {
    object PatientList : Screen("patient_list_screen")

    object PatientDetails : Screen(
        "patient_details_screen?$PATIENT_DETAILS_ARG_KEY={$PATIENT_DETAILS_ARG_KEY}"
    ) {
        fun passPatientId(patientId: Int? = null): String {
            return "patient_details_screen?$PATIENT_DETAILS_ARG_KEY=$patientId"
        }
    }
}
