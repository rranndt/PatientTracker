package dev.rranndt.patienttracker.presentation.patient_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rranndt.patienttracker.domain.model.Patient
import dev.rranndt.patienttracker.domain.repository.PatientRepository
import dev.rranndt.patienttracker.util.Constants.PATIENT_DETAILS_ARG_KEY
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatientDetailsViewModel @Inject constructor(
    private val repository: PatientRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(PatientDetailsUiState())
    val state: StateFlow<PatientDetailsUiState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private var currentPatientId: Int? = null

    init {
        fetchPatientDetails()
    }

    fun onEvent(event: PatientsDetailsEvents) {
        when (event) {
            is PatientsDetailsEvents.EnteredName -> {
                _state.update { currentsState ->
                    currentsState.copy(name = event.name)
                }
            }

            is PatientsDetailsEvents.EnteredAge -> {
                if (event.age.length <= 2) {
                    _state.update { currentsState ->
                        currentsState.copy(age = event.age)
                    }
                }
            }

            is PatientsDetailsEvents.EnteredAssignedDoctor -> {
                _state.update { currentsState ->
                    currentsState.copy(doctorAssigned = event.doctorAssigned)
                }
            }

            is PatientsDetailsEvents.EnteredPrescription -> {
                _state.update { currentsState ->
                    currentsState.copy(prescription = event.prescription)
                }
            }

            PatientsDetailsEvents.SelectedFemale -> {
                _state.update { currentsState ->
                    currentsState.copy(gender = 2)
                }
            }

            PatientsDetailsEvents.SelectedMale -> {
                _state.update { currentsState ->
                    currentsState.copy(gender = 1)
                }
            }

            PatientsDetailsEvents.SaveButton -> {
                viewModelScope.launch {
                    try {
                        savePatient()
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowToast(
                                message = e.message ?: "Couldn't save patient's details"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun savePatient() {
        val age = _state.value.age.toIntOrNull()
        when {
            _state.value.name.isEmpty() -> throw TextFieldException("Please enter name.")
            _state.value.age.isEmpty() -> throw TextFieldException("Please enter age.")
            _state.value.gender == 0 -> throw TextFieldException("Please enter gender.")
            _state.value.doctorAssigned.isEmpty() -> throw TextFieldException("Please enter doctor assigned.")
            _state.value.prescription.isEmpty() -> throw TextFieldException("Please enter prescription.")
            age == null || age < 0 || age >= 100 -> throw TextFieldException("please enter valid age.")
        }
        val trimmedName = _state.value.name.trim()
        val trimmedDoctorAssigned = _state.value.doctorAssigned.trim()
        viewModelScope.launch {
            repository.addOrUpdatePatient(
                patient = Patient(
                    name = trimmedName,
                    age = _state.value.age,
                    gender = _state.value.gender,
                    doctorAssigned = trimmedDoctorAssigned,
                    prescription = _state.value.prescription,
                    patientId = currentPatientId
                )
            )
        }
    }

    private fun fetchPatientDetails() {
        savedStateHandle.get<Int>(key = PATIENT_DETAILS_ARG_KEY)?.let { patientId ->
            if (patientId != -1) {
                viewModelScope.launch {
                    repository.getPatientById(patientId)?.apply {
                        _state.update { currentState ->
                            currentState.copy(
                                name = name,
                                age = age,
                                gender = gender,
                                doctorAssigned = doctorAssigned,
                                prescription = prescription
                            )
                        }
                        currentPatientId = patientId
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}

class TextFieldException(message: String?) : Exception(message)