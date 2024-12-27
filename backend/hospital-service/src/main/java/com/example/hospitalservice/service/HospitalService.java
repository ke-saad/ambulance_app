package com.example.hospitalservice.service;

import com.example.hospitalservice.model.Hospital;
import com.example.hospitalservice.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    public Hospital getHospitalById(Long id) {
        Optional<Hospital> optionalHospital = hospitalRepository.findById(id);
        return optionalHospital.orElse(null);
    }

    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Hospital updateHospital(Long id, Hospital updatedHospital) {
        Optional<Hospital> optionalHospital = hospitalRepository.findById(id);
        if(optionalHospital.isPresent()){
            Hospital existingHospital = optionalHospital.get();
            existingHospital.setName(updatedHospital.getName());
            existingHospital.setAddress(updatedHospital.getAddress());
            existingHospital.setPhone(updatedHospital.getPhone());
            existingHospital.setEmail(updatedHospital.getEmail());
            return  hospitalRepository.save(existingHospital);
        }
        return null;
    }

    public void deleteHospital(Long id) {
        hospitalRepository.deleteById(id);
    }

    public boolean hospitalOffersSpecialty(Hospital hospital, String specialty) {
        return hospital.getSpecialties() != null && hospital.getSpecialties().contains(specialty);
    }
}