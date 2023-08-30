package dev.rranndt.patienttracker.presentation.patient_list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.rranndt.patienttracker.domain.model.Patient

@Composable
fun PatientItem(
    patient: Patient,
    showDialog: Boolean,
    onItemClicked: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogConfirm: () -> Unit,
    onOpenDialogClicked: () -> Unit
) {
    if (showDialog) {
        DeleteDialog(
            title = "Delete",
            message = "Are you sure, you want to delete " +
                    "patient \"${patient.name}\" from patients list.",
            onDialogDismiss = onDialogDismiss,
            onConfirmButtonClicked = {
                onDeleteConfirm()
                onDialogConfirm()
            }
        )
    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.clickable { onItemClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp),
        ) {
            Column(modifier = Modifier.weight(9f)) {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Assigned to ${patient.doctorAssigned}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(
                onClick = onOpenDialogClicked,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}