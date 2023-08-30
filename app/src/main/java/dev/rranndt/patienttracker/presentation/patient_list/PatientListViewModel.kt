package dev.rranndt.patienttracker.presentation.patient_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rranndt.patienttracker.domain.model.Patient
import dev.rranndt.patienttracker.domain.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientListViewModel @Inject constructor(
    private val repository: PatientRepository
) : ViewModel() {
    private val _patientList = MutableStateFlow<List<Patient>>(emptyList())
    val patientList: StateFlow<List<Patient>> = _patientList.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPatient().collect {
                _patientList.value = it
            }
        }
    }

    fun onEvent(event: PatientsListEvents) {
        when (event) {
            is PatientsListEvents.DeletePatient -> deletePatient(event.patient)

            PatientsListEvents.OnDialogConfirm -> {
                _showDialog.value = false
            }

            PatientsListEvents.OnDialogDismiss -> {
                _showDialog.value = false
            }

            PatientsListEvents.OnOpenDialogClicked -> {
                _showDialog.value = true
            }
        }
    }

    private fun deletePatient(patient: Patient) {
        viewModelScope.launch {
            repository.deletePatient(patient)
        }
    }
}