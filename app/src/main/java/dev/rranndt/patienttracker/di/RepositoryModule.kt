package dev.rranndt.patienttracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rranndt.patienttracker.data.local.PatientDatabase
import dev.rranndt.patienttracker.data.repository.PatientRepositoryImpl
import dev.rranndt.patienttracker.domain.repository.PatientRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePatientRepository(db: PatientDatabase): PatientRepository =
        PatientRepositoryImpl(dao = db.patientDao)
}