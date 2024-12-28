package com.example.ambulanceservice.service;

import com.example.ambulanceservice.model.Ambulance;
import com.example.ambulanceservice.model.enums.Specialty;
import com.example.ambulanceservice.model.enums.AmbulanceStatus;
import com.example.ambulanceservice.repository.AmbulanceRepository;
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
public class AmbulanceServiceTest {

    @Mock
    private AmbulanceRepository ambulanceRepository;

    @InjectMocks
    private AmbulanceService ambulanceService;

    private Ambulance ambulance1;
    private Ambulance ambulance2;
    @BeforeEach
    void setUp(){
        ambulance1 = new Ambulance(1L, "KA-01-AB-1234", "Model X", AmbulanceStatus.Available, 34.0522, -118.2437, Specialty.General, "555-111-1111");
        ambulance2 = new Ambulance(2L, "MH-02-CD-5678", "Model Y", AmbulanceStatus.Busy, 37.7749, -122.4194, Specialty.Cardiology, "555-222-2222");

    }

    @Test
    void testGetAllAmbulances(){
        when(ambulanceRepository.findAll()).thenReturn(Arrays.asList(ambulance1,ambulance2));
        List<Ambulance> ambulances = ambulanceService.getAllAmbulances();

        assertNotNull(ambulances);
        assertEquals(2, ambulances.size());
        verify(ambulanceRepository).findAll();
    }

    @Test
    void testGetAmbulanceById_Success(){
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(ambulance1));

        Ambulance ambulance = ambulanceService.getAmbulanceById(1L);

        assertNotNull(ambulance);
        assertEquals(ambulance1.getId(), ambulance.getId());
        assertEquals(ambulance1.getModel(), ambulance.getModel());
        verify(ambulanceRepository).findById(1L);
    }

    @Test
    void testGetAmbulanceById_NotFound(){
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.empty());
        Ambulance ambulance = ambulanceService.getAmbulanceById(1L);
        assertNull(ambulance);
        verify(ambulanceRepository).findById(1L);
    }

    @Test
    void testCreateAmbulance(){
        when(ambulanceRepository.save(ambulance1)).thenReturn(ambulance1);
        Ambulance savedAmbulance = ambulanceService.createAmbulance(ambulance1);
        assertEquals(ambulance1, savedAmbulance);
        verify(ambulanceRepository).save(ambulance1);
    }

    @Test
    void testUpdateAmbulance_Success(){
        Ambulance updatedAmbulance = new Ambulance(1L, "KA-01-AB-1234", "New Model", AmbulanceStatus.Busy, 35.0522, -119.2437, Specialty.General, "555-111-9999");
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(ambulance1));
        when(ambulanceRepository.save(any(Ambulance.class))).thenReturn(updatedAmbulance);
        Ambulance result = ambulanceService.updateAmbulance(1L, updatedAmbulance);
        assertNotNull(result);
        assertEquals("New Model", result.getModel());
        assertEquals(AmbulanceStatus.Busy, result.getStatus());
        assertEquals(35.0522, result.getLatitude());
        verify(ambulanceRepository).findById(1L);
        verify(ambulanceRepository).save(any(Ambulance.class));
    }


    @Test
    void testUpdateAmbulance_NotFound(){
        Ambulance updatedAmbulance = new Ambulance(1L, "KA-01-AB-1234", "New Model", AmbulanceStatus.Busy, 35.0522, -119.2437, Specialty.General, "555-111-9999");
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.empty());
        Ambulance result = ambulanceService.updateAmbulance(1L, updatedAmbulance);
        assertNull(result);
        verify(ambulanceRepository).findById(1L);
        verify(ambulanceRepository, never()).save(any(Ambulance.class));
    }

    @Test
    void testUpdateAmbulanceLocation(){
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.of(ambulance1));
        when(ambulanceRepository.save(any(Ambulance.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Ambulance result = ambulanceService.updateAmbulanceLocation(1L, 35, -119);

        assertNotNull(result);
        assertEquals(35, result.getLatitude());
        assertEquals(-119, result.getLongitude());
        verify(ambulanceRepository).findById(1L);
        verify(ambulanceRepository).save(any(Ambulance.class));
    }

    @Test
    void testUpdateAmbulanceLocation_NotFound() {
        when(ambulanceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            ambulanceService.updateAmbulanceLocation(1L, 35, -119);
        });

        verify(ambulanceRepository).findById(1L);
        verify(ambulanceRepository, never()).save(any(Ambulance.class));
    }

    @Test
    void testDeleteAmbulance(){
        ambulanceService.deleteAmbulance(1L);
        verify(ambulanceRepository).deleteById(1L);
    }

}
