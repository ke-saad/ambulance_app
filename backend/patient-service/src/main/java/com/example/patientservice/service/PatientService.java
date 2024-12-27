package com.example.patientservice.service;

import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        return optionalPatient.orElse(null);
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()){
            Patient existingPatient = optionalPatient.get();
            existingPatient.setFirstName(updatedPatient.getFirstName());
            existingPatient.setLastName(updatedPatient.getLastName());
            existingPatient.setGender(updatedPatient.getGender());
            existingPatient.setDateOfBirth(updatedPatient.getDateOfBirth());
            existingPatient.setAddress(updatedPatient.getAddress());
            existingPatient.setPhone(updatedPatient.getPhone());
            return patientRepository.save(existingPatient);
        }
        return null;

    }
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}