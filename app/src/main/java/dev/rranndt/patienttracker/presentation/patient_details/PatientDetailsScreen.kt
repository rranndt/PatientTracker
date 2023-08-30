package dev.rranndt.patienttracker.presentation.patient_details

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    onBackClicked: () -> Unit,
    onSuccessfullySaving: () -> Unit,
) {
    val viewModel: PatientDetailsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequester.requestFocus()
    }

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                PatientDetailsViewModel.UiEvent.SaveNote -> {
                    onSuccessfullySaving()
                    Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show()
                }

                is PatientDetailsViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(onBackClicked = onBackClicked)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp)
                .padding(paddingValues = it)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { newValue ->
                    viewModel.onEvent(PatientsDetailsEvents.EnteredName(newValue))
                },
                label = { Text(text = "Name") },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.age,
                    onValueChange = { newValue ->
                        viewModel.onEvent(PatientsDetailsEvents.EnteredAge(newValue))
                    },
                    label = { Text(text = "Age") },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }),
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(10.dp))
                RadioGroup(
                    text = "Male",
                    selected = state.gender == 1,
                    onClick = { viewModel.onEvent(PatientsDetailsEvents.SelectedMale) },
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                RadioGroup(
                    text = "Female",
                    selected = state.gender == 2,
                    onClick = { viewModel.onEvent(PatientsDetailsEvents.SelectedFemale) },
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.doctorAssigned,
                onValueChange = { newValue ->
                    viewModel.onEvent(PatientsDetailsEvents.EnteredAssignedDoctor(newValue))
                },
                label = { Text(text = "Assigned Doctor's Name") },
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.prescription,
                onValueChange = { newValue ->
                    viewModel.onEvent(PatientsDetailsEvents.EnteredPrescription(newValue))
                },
                label = { Text(text = "Prescription") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.onEvent(PatientsDetailsEvents.SaveButton) }
            ) {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun RadioGroup(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBackClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Patient's Details Screen",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    )
}

// States :- Anything that can change during the usage of app.
// Events :- Anything that can user do is event.