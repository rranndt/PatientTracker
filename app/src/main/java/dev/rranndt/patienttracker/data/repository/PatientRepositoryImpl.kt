package dev.rranndt.patienttracker.data.repository

import dev.rranndt.patienttracker.data.local.PatientDao
import dev.rranndt.patienttracker.domain.model.Patient
import dev.rranndt.patienttracker.domain.repository.PatientRepository
import kotlinx.coroutines.flow.Flow

class PatientRepositoryImpl(
    private val dao: PatientDao
) : PatientRepository {
    override suspend fun addOrUpdatePatient(patient: Patient) = dao.addOrUpdatePatient(patient)

    override suspend fun deletePatient(patient: Patient) = dao.deletePatient(patient)

    override suspend fun getPatientById(patientId: Int): Patient? = dao.getPatientById(patientId)

    override fun getAllPatient(): Flow<List<Patient>> = dao.getAllPatient()
}