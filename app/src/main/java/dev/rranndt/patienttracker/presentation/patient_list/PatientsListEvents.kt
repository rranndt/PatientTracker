package dev.rranndt.patienttracker.presentation.patient_list

import dev.rranndt.patienttracker.domain.model.Patient

sealed class PatientsListEvents {
    data class DeletePatient(val patient: Patient) : PatientsListEvents()

    object OnOpenDialogClicked : PatientsListEvents()

    object OnDialogConfirm : PatientsListEvents()

    object OnDialogDismiss : PatientsListEvents()
}
