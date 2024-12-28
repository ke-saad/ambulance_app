package com.example.patientservice.service;

import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient1;
    private Patient patient2;


    @BeforeEach
    void setUp() {
        patient1 = new Patient(1L, "John", "Doe", "Male", "1980-05-15", "555-111-2222", "123 Main St, Anytown");
        patient2 = new Patient(2L, "Jane", "Smith", "Female", "1992-11-02", "555-333-4444", "456 Oak Ave, Springfield");

    }

    @Test
    void testGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));
        List<Patient> patients = patientService.getAllPatients();

        assertNotNull(patients);
        assertEquals(2, patients.size());
        verify(patientRepository).findAll();
    }

    @Test
    void testGetPatientById_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));

        Patient patient = patientService.getPatientById(1L);

        assertNotNull(patient);
        assertEquals(patient1.getId(), patient.getId());
        assertEquals(patient1.getFirstName(), patient.getFirstName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        Patient patient = patientService.getPatientById(1L);
        assertNull(patient);
        verify(patientRepository).findById(1L);
    }

    @Test
    void testCreatePatient() {
        when(patientRepository.save(patient1)).thenReturn(patient1);
        Patient savedPatient = patientService.createPatient(patient1);
        assertEquals(patient1, savedPatient);
        verify(patientRepository).save(patient1);
    }

    @Test
    void testUpdatePatient_Success() {
        Patient updatedPatient = new Patient(1L, "UpdatedJohn", "UpdatedDoe", "Other", "1985-06-16", "555-999-9999", "999 New Rd");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient1));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        Patient result = patientService.updatePatient(1L, updatedPatient);

        assertNotNull(result);
        assertEquals("UpdatedJohn", result.getFirstName());
        assertEquals("UpdatedDoe", result.getLastName());
        assertEquals("Other", result.getGender());
        assertEquals("1985-06-16", result.getDateOfBirth());
        assertEquals("555-999-9999", result.getPhone());
        assertEquals("999 New Rd", result.getAddress());
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
    }


    @Test
    void testUpdatePatient_NotFound(){
        Patient updatedPatient = new Patient(1L, "UpdatedJohn", "UpdatedDoe", "Other", "1985-06-16", "555-999-9999", "999 New Rd");
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        Patient result = patientService.updatePatient(1L, updatedPatient);
        assertNull(result);
        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).save(any(Patient.class));
    }


    @Test
    void testDeletePatient() {
        patientService.deletePatient(1L);
        verify(patientRepository).deleteById(1L);
    }
}