package dev.rranndt.patienttracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.rranndt.patienttracker.domain.model.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Upsert
    suspend fun addOrUpdatePatient(patient: Patient)

    @Delete
    suspend fun deletePatient(patient: Patient)

    @Query("SELECT * FROM patientTable WHERE patientId = :patientId")
    suspend fun getPatientById(patientId: Int): Patient?

    @Query("SELECT * FROM patientTable")
    fun getAllPatient(): Flow<List<Patient>>
}