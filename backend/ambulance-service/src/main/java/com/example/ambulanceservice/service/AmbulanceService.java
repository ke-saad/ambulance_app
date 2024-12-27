package com.example.ambulanceservice.service;

import com.example.ambulanceservice.model.Ambulance;
import com.example.ambulanceservice.repository.AmbulanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceService {

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    public List<Ambulance> getAllAmbulances() {
        return ambulanceRepository.findAll();
    }

    public Ambulance getAmbulanceById(Long id) {
        Optional<Ambulance> optionalAmbulance = ambulanceRepository.findById(id);
        return optionalAmbulance.orElse(null);
    }

    public Ambulance createAmbulance(Ambulance ambulance) {
        return ambulanceRepository.save(ambulance);
    }

    public Ambulance updateAmbulance(Long id, Ambulance updatedAmbulance) {
        Optional<Ambulance> optionalAmbulance = ambulanceRepository.findById(id);
        if (optionalAmbulance.isPresent()) {
            Ambulance existingAmbulance = optionalAmbulance.get();
            existingAmbulance.setRegistrationNumber(updatedAmbulance.getRegistrationNumber());
            existingAmbulance.setModel(updatedAmbulance.getModel());
            existingAmbulance.setStatus(updatedAmbulance.getStatus());
            existingAmbulance.setLatitude(updatedAmbulance.getLatitude());
            existingAmbulance.setLongitude(updatedAmbulance.getLongitude());
            existingAmbulance.setSpecialty(updatedAmbulance.getSpecialty());
            existingAmbulance.setPhone(updatedAmbulance.getPhone());
            return ambulanceRepository.save(existingAmbulance);
        }
        return null;
    }

    public Ambulance updateAmbulanceLocation(Long id, double latitude, double longitude) {
        Ambulance ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulance not found with id: " + id));

        ambulance.setLatitude(latitude);
        ambulance.setLongitude(longitude);

        return ambulanceRepository.save(ambulance);
    }

    public void deleteAmbulance(Long id) {
        ambulanceRepository.deleteById(id);
    }
}