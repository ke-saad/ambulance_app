package com.example.routingservice.service;

import com.example.common.dto.AmbulanceDto;
import com.example.common.dto.HospitalDto;
import com.example.routingservice.client.AmbulanceClient;
import com.example.routingservice.client.HospitalClient;
import com.example.routingservice.client.TrafficApiClient;
import com.example.routingservice.dto.NearestAmbulanceDto;
import com.example.routingservice.model.Routing;
import com.example.routingservice.repository.RoutingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RoutingServiceTest {

    @Mock
    private TrafficApiClient trafficApiClient;

    @Mock
    private RoutingRepository routingRepository;

    @Mock
    private HospitalClient hospitalClient;

    @Mock
    private AmbulanceClient ambulanceClient;

    @InjectMocks
    private RoutingService routingService;


    private AmbulanceDto ambulance1;
    private AmbulanceDto ambulance2;
    private HospitalDto hospital1;
    private Routing routing1;

    private final String apiKey = "test-api-key";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(routingService, "apiKey", apiKey);

        ambulance1 = new AmbulanceDto();
        ambulance1.setId(1L);
        ambulance1.setRegistrationNumber("KA-01-AB-1234");
        ambulance1.setModel("Model X");
        ambulance1.setStatus("Available");
        ambulance1.setLatitude(34.0522);
        ambulance1.setLongitude(-118.2437);
        ambulance1.setSpecialty("General");
        ambulance1.setPhone("555-111-1111");

        ambulance2 = new AmbulanceDto();
        ambulance2.setId(2L);
        ambulance2.setRegistrationNumber("MH-02-CD-5678");
        ambulance2.setModel("Model Y");
        ambulance2.setStatus("Available");
        ambulance2.setLatitude(37.7749);
        ambulance2.setLongitude(-122.4194);
        ambulance2.setSpecialty("Cardiology");
        ambulance2.setPhone("555-222-2222");

        hospital1 = new HospitalDto();
        hospital1.setId(1L);
        hospital1.setName("City General Hospital");
        hospital1.setAddress("123 Main St, Anytown");
        hospital1.setPhone("555-123-4567");
        hospital1.setEmail("info@citygeneral.com");
        hospital1.setLatitude(34.0522);
        hospital1.setLongitude(-118.2437);
        hospital1.setSpecialties(Arrays.asList("Cardiology", "Emergency"));

        routing1 = new Routing(1L, "AmbulanceService", "HospitalService", "{'location': 'near', 'specialty': 'cardiology'}", "Route to nearest hospital with cardiology");
    }

    @Test
    void testGetAllRoutings() {
        when(routingRepository.findAll()).thenReturn(Arrays.asList(routing1));
        List<Routing> routings = routingService.getAllRoutings();

        assertNotNull(routings);
        assertEquals(1, routings.size());
        verify(routingRepository).findAll();
    }

    @Test
    void testGetRoutingById_Success() {
        when(routingRepository.findById(1L)).thenReturn(Optional.of(routing1));

        Routing routing = routingService.getRoutingById(1L);

        assertNotNull(routing);
        assertEquals(routing1.getId(), routing.getId());
        assertEquals(routing1.getFromService(), routing.getFromService());
        verify(routingRepository).findById(1L);
    }
    @Test
    void testGetRoutingById_NotFound() {
        when(routingRepository.findById(1L)).thenReturn(Optional.empty());
        Routing routing = routingService.getRoutingById(1L);
        assertNull(routing);
        verify(routingRepository).findById(1L);
    }

    @Test
    void testCreateRouting() {
        when(routingRepository.save(routing1)).thenReturn(routing1);
        Routing savedRouting = routingService.createRouting(routing1);
        assertEquals(routing1, savedRouting);
        verify(routingRepository).save(routing1);
    }

    @Test
    void testUpdateRouting_Success(){
        Routing updatedRouting = new Routing(1L, "NewService", "NewToService", "{'location': 'far'}", "Route to far hospital");
        when(routingRepository.findById(1L)).thenReturn(Optional.of(routing1));
        when(routingRepository.save(any(Routing.class))).thenReturn(updatedRouting);

        Routing result = routingService.updateRouting(1L, updatedRouting);

        assertNotNull(result);
        assertEquals("NewService", result.getFromService());
        assertEquals("NewToService", result.getToService());
        assertEquals("{'location': 'far'}", result.getCriteria());
        assertEquals("Route to far hospital", result.getDecision());
        verify(routingRepository).findById(1L);
        verify(routingRepository).save(any(Routing.class));
    }

    @Test
    void testUpdateRouting_NotFound() {
        Routing updatedRouting = new Routing(1L, "NewService", "NewToService", "{'location': 'far'}", "Route to far hospital");
        when(routingRepository.findById(1L)).thenReturn(Optional.empty());

        Routing result = routingService.updateRouting(1L, updatedRouting);
        assertNull(result);
        verify(routingRepository).findById(1L);
        verify(routingRepository, never()).save(any(Routing.class));

    }

    @Test
    void testDeleteRouting() {
        routingService.deleteRouting(1L);
        verify(routingRepository).deleteById(1L);
    }


    @Test
    void testGetAllAmbulances() {
        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1, ambulance2));
        List<AmbulanceDto> ambulances = routingService.getAllAmbulances();

        assertNotNull(ambulances);
        assertEquals(2, ambulances.size());
        verify(ambulanceClient).getAllAmbulances();
    }

    @Test
    void testGetAmbulanceById() {
        when(ambulanceClient.getAmbulanceById(1L)).thenReturn(ambulance1);
        AmbulanceDto ambulance = routingService.getAmbulanceById(1L);

        assertNotNull(ambulance);
        assertEquals(ambulance1.getId(), ambulance.getId());
        verify(ambulanceClient).getAmbulanceById(1L);
    }

    @Test
    void testGetAllHospitals() {
        when(hospitalClient.getAllHospitals()).thenReturn(Arrays.asList(hospital1));
        List<HospitalDto> hospitals = routingService.getAllHospitals();

        assertNotNull(hospitals);
        assertEquals(1, hospitals.size());
        verify(hospitalClient).getAllHospitals();
    }

    @Test
    void testGetHospitalById() {
        when(hospitalClient.getHospitalById(1L)).thenReturn(hospital1);
        HospitalDto hospital = routingService.getHospitalById(1L);

        assertNotNull(hospital);
        assertEquals(hospital1.getId(), hospital.getId());
        verify(hospitalClient).getHospitalById(1L);
    }

    @Test
    void testFindNearestAmbulance_NoAmbulances() {
        when(ambulanceClient.getAllAmbulances()).thenReturn(Collections.emptyList());
        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, null);
        assertNull(result);
        verify(ambulanceClient).getAllAmbulances();
    }


    @Test
    void testFindNearestAmbulance_NoAmbulancesWithSpecialty() {
        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1));
        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, "Cardiology");
        assertNull(result);
        verify(ambulanceClient).getAllAmbulances();
    }
    @Test
    void testFindNearestAmbulance_WithSpecialty() {
        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1, ambulance2));
        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, "Cardiology");
        assertNotNull(result);
        assertEquals(ambulance2.getId(), result.getId());
        verify(ambulanceClient).getAllAmbulances();

    }
    @Test
    void testFindNearestAmbulance_FallbackToDistance_SingleAmbulance(){
        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1));
        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey))).thenReturn(null);


        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, null);

        assertNotNull(result);
        assertEquals(ambulance1.getId(), result.getId());
        verify(ambulanceClient).getAllAmbulances();
        verify(trafficApiClient).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));

    }

    @Test
    void testFindNearestAmbulance_FallbackToDistance_MultipleAmbulances() {
        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1, ambulance2));
        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey))).thenReturn(null);

        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, null);

        assertNotNull(result);
        assertEquals(ambulance1.getId(), result.getId());
        verify(ambulanceClient).getAllAmbulances();
        verify(trafficApiClient, times(2)).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));
    }


    @Test
    void testFindNearestAmbulance_WithTraffic_SingleAmbulance() {

        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1));
        Map<String, Object> mockTrafficData = new HashMap<>();
        Map<String, Object> flowSegmentData = new HashMap<>();
        flowSegmentData.put("currentSpeed", 50);
        mockTrafficData.put("flowSegmentData", flowSegmentData);
        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey))).thenReturn(mockTrafficData);


        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, null);


        assertNotNull(result);
        assertEquals(ambulance1.getId(), result.getId());
        verify(ambulanceClient).getAllAmbulances();
        verify(trafficApiClient).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));
    }


    @Test
    void testFindNearestAmbulance_WithTraffic_MultipleAmbulances() {

        when(ambulanceClient.getAllAmbulances()).thenReturn(Arrays.asList(ambulance1, ambulance2));
        Map<String, Object> mockTrafficData = new HashMap<>();
        Map<String, Object> flowSegmentData = new HashMap<>();
        flowSegmentData.put("currentSpeed", 50);
        mockTrafficData.put("flowSegmentData", flowSegmentData);
        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey))).thenReturn(mockTrafficData);

        NearestAmbulanceDto result = routingService.findNearestAmbulance(34.0, -118.0, null);

        assertNotNull(result);
        assertEquals(ambulance1.getId(), result.getId());
        verify(ambulanceClient).getAllAmbulances();
        verify(trafficApiClient, times(2)).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));
    }


    @Test
    void testGetAverageSpeedFromTrafficApi_Success() {
        Map<String, Object> mockTrafficData = new HashMap<>();
        Map<String, Object> flowSegmentData = new HashMap<>();
        flowSegmentData.put("currentSpeed", 60);
        mockTrafficData.put("flowSegmentData", flowSegmentData);

        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey)))
                .thenReturn(mockTrafficData);

        double speed = ReflectionTestUtils.invokeMethod(routingService, "getAverageSpeedFromTrafficApi", "34.0522,-118.2437");
        assertEquals(60.0, speed);
        verify(trafficApiClient).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));

    }

    @Test
    void testGetAverageSpeedFromTrafficApi_NoData() {
        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey)))
                .thenReturn(null);
        double speed = ReflectionTestUtils.invokeMethod(routingService, "getAverageSpeedFromTrafficApi", "34.0522,-118.2437");
        assertEquals(0.0, speed);
        verify(trafficApiClient).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));
    }

    @Test
    void testGetAverageSpeedFromTrafficApi_MissingSpeed() {
        Map<String, Object> mockTrafficData = new HashMap<>();
        Map<String, Object> flowSegmentData = new HashMap<>();
        mockTrafficData.put("flowSegmentData", flowSegmentData);
        when(trafficApiClient.getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey)))
                .thenReturn(mockTrafficData);
        double speed = ReflectionTestUtils.invokeMethod(routingService, "getAverageSpeedFromTrafficApi", "34.0522,-118.2437");
        assertEquals(0.0, speed);
        verify(trafficApiClient).getFlowSegmentData(anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString(), anyBoolean(), eq(apiKey));
    }

    @Test
    void testCalculateDistance() {
        double distance = ReflectionTestUtils.invokeMethod(routingService, "calculateDistance", 34.0522, -118.2437, 34.0523, -118.2438);
        assertNotEquals(0.0, distance);
    }

    @Test
    void testMapToNearestAmbulanceDto(){
        NearestAmbulanceDto nearestAmbulanceDto = ReflectionTestUtils.invokeMethod(routingService, "mapToNearestAmbulanceDto", ambulance1, 10.0);
        assertEquals(ambulance1.getId(), nearestAmbulanceDto.getId());
        assertEquals(ambulance1.getRegistrationNumber(), nearestAmbulanceDto.getRegistrationNumber());
        assertEquals(10.0, nearestAmbulanceDto.getDistance());
        assertEquals(ambulance1.getLatitude(), nearestAmbulanceDto.getLatitude());
        assertEquals(ambulance1.getLongitude(), nearestAmbulanceDto.getLongitude());
    }
}