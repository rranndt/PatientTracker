package dev.rranndt.patienttracker.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rranndt.patienttracker.data.local.PatientDatabase
import dev.rranndt.patienttracker.util.Constants.PATIENT_DATABASE
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePatientDatabase(app: Application): PatientDatabase =
        Room.databaseBuilder(
            app,
            PatientDatabase::class.java,
            PATIENT_DATABASE
        ).build()
}