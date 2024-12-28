package com.example.hospitalservice.service;

import com.example.hospitalservice.model.Hospital;
import com.example.hospitalservice.repository.HospitalRepository;
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
public class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private HospitalService hospitalService;

    private Hospital hospital1;
    private Hospital hospital2;

    @BeforeEach
    void setUp() {
        hospital1 = new Hospital(1L, "City General Hospital", "123 Main St, Anytown", "555-123-4567", "info@citygeneral.com", 34.0522, -118.2437, Arrays.asList("Cardiology", "Emergency"));
        hospital2 = new Hospital(2L, "St. Mary's Medical Center", "456 Oak Ave, Springfield", "555-987-6543", "contact@stmarys.org", 37.7749, -122.4194, Arrays.asList("Oncology", "Pediatrics"));

    }

    @Test
    void testGetAllHospitals() {
        when(hospitalRepository.findAll()).thenReturn(Arrays.asList(hospital1, hospital2));
        List<Hospital> hospitals = hospitalService.getAllHospitals();

        assertNotNull(hospitals);
        assertEquals(2, hospitals.size());
        verify(hospitalRepository).findAll();
    }

    @Test
    void testGetHospitalById_Success() {
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital1));

        Hospital hospital = hospitalService.getHospitalById(1L);

        assertNotNull(hospital);
        assertEquals(hospital1.getId(), hospital.getId());
        assertEquals(hospital1.getName(), hospital.getName());
        verify(hospitalRepository).findById(1L);
    }

    @Test
    void testGetHospitalById_NotFound() {
        when(hospitalRepository.findById(1L)).thenReturn(Optional.empty());
        Hospital hospital = hospitalService.getHospitalById(1L);
        assertNull(hospital);
        verify(hospitalRepository).findById(1L);
    }

    @Test
    void testCreateHospital() {
        when(hospitalRepository.save(hospital1)).thenReturn(hospital1);
        Hospital savedHospital = hospitalService.createHospital(hospital1);
        assertEquals(hospital1, savedHospital);
        verify(hospitalRepository).save(hospital1);
    }

    @Test
    void testUpdateHospital_Success() {
        Hospital updatedHospital = new Hospital(1L, "Updated Name", "Updated Address", "555-111-9999", "updated@email.com", 35.0522, -119.2437, Arrays.asList("Cardiology"));

        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital1));
        when(hospitalRepository.save(any(Hospital.class))).thenReturn(updatedHospital);

        Hospital result = hospitalService.updateHospital(1L, updatedHospital);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Address", result.getAddress());
        assertEquals("555-111-9999", result.getPhone());
        assertEquals("updated@email.com", result.getEmail());
        verify(hospitalRepository).findById(1L);
        verify(hospitalRepository).save(any(Hospital.class));
    }

    @Test
    void testUpdateHospital_NotFound() {
        Hospital updatedHospital = new Hospital(1L, "Updated Name", "Updated Address", "555-111-9999", "updated@email.com", 35.0522, -119.2437, Arrays.asList("Cardiology"));

        when(hospitalRepository.findById(1L)).thenReturn(Optional.empty());
        Hospital result = hospitalService.updateHospital(1L, updatedHospital);
        assertNull(result);
        verify(hospitalRepository).findById(1L);
        verify(hospitalRepository, never()).save(any(Hospital.class));
    }
    @Test
    void testDeleteHospital() {
        hospitalService.deleteHospital(1L);
        verify(hospitalRepository).deleteById(1L);
    }

    @Test
    void testHospitalOffersSpecialty_True() {
        assertTrue(hospitalService.hospitalOffersSpecialty(hospital1, "Cardiology"));
    }

    @Test
    void testHospitalOffersSpecialty_False() {
        assertFalse(hospitalService.hospitalOffersSpecialty(hospital1, "Neurology"));
    }

    @Test
    void testHospitalOffersSpecialty_NullSpecialties() {
        Hospital hospital = new Hospital(1L, "Test Hospital", "Test Address", "123-456-7890", "test@test.com", 34.0522, -118.2437, null);
        assertFalse(hospitalService.hospitalOffersSpecialty(hospital, "Cardiology"));
    }

}
