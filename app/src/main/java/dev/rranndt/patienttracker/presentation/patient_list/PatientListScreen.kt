package dev.rranndt.patienttracker.presentation.patient_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.rranndt.patienttracker.presentation.patient_list.component.PatientItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    onFabClicked: () -> Unit,
    onItemClicked: (Int?) -> Unit
) {
    val viewModel: PatientListViewModel = hiltViewModel()
    val patientList by viewModel.patientList.collectAsStateWithLifecycle()
    val showDialogState by viewModel.showDialog.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { ListAppBar() },
        floatingActionButton = {
            ListFab(onFabClicked = onFabClicked)
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
        ) {
            items(patientList) { patient ->
                PatientItem(
                    patient = patient,
                    showDialog = showDialogState,
                    onItemClicked = { onItemClicked(patient.patientId) },
                    onDeleteConfirm = { viewModel.onEvent(PatientsListEvents.DeletePatient(patient)) },
                    onDialogDismiss = { viewModel.onEvent(PatientsListEvents.OnDialogDismiss) },
                    onDialogConfirm = { viewModel.onEvent(PatientsListEvents.OnDialogConfirm) },
                    onOpenDialogClicked = { viewModel.onEvent(PatientsListEvents.OnOpenDialogClicked) }
                )
            }
        }
        if (patientList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = it),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add patients details\nby pressing add button.",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListAppBar() {
    TopAppBar(
        title = {
            Text(
                text = "Patient Tracker",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun ListFab(
    onFabClicked: () -> Unit,
) {
    FloatingActionButton(
        onClick = onFabClicked,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add patient button"
        )
    }
}