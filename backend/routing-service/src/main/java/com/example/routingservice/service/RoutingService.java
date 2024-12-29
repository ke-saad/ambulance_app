package com.example.routingservice.service;

import com.example.common.dto.AmbulanceDto;
import com.example.common.dto.HospitalDto;
import com.example.routingservice.client.AmbulanceClient;
import com.example.routingservice.client.HospitalClient;
import com.example.routingservice.client.TrafficApiClient;
import com.example.routingservice.dto.NearestAmbulanceDto;
import com.example.routingservice.model.Routing;
import com.example.routingservice.repository.RoutingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoutingService {

    private static final Logger log = LoggerFactory.getLogger(RoutingService.class);

    @Autowired
    private TrafficApiClient trafficApiClient;

    @Autowired
    private RoutingRepository routingRepository;

    @Autowired
    private HospitalClient hospitalClient;

    @Autowired
    private AmbulanceClient ambulanceClient;

    @Value("${traffic.api.key}")
    private String apiKey;

    public List<Routing> getAllRoutings() {
        return routingRepository.findAll();
    }

    public Routing getRoutingById(Long id) {
        Optional<Routing> optionalRouting = routingRepository.findById(id);
        return optionalRouting.orElse(null);
    }

    public Routing createRouting(Routing routing) {
        return routingRepository.save(routing);
    }

    public Routing updateRouting(Long id, Routing updatedRouting) {
        Optional<Routing> optionalRouting = routingRepository.findById(id);
        if (optionalRouting.isPresent()) {
            Routing existingRouting = optionalRouting.get();
            existingRouting.setFromService(updatedRouting.getFromService());
            existingRouting.setToService(updatedRouting.getToService());
            existingRouting.setCriteria(updatedRouting.getCriteria());
            existingRouting.setDecision(updatedRouting.getDecision());
            return routingRepository.save(existingRouting);
        }
        return null;
    }

    public void deleteRouting(Long id) {
        routingRepository.deleteById(id);
    }

    public List<AmbulanceDto> getAllAmbulances() {
        return ambulanceClient.getAllAmbulances();
    }

    public AmbulanceDto getAmbulanceById(Long id) {
        return ambulanceClient.getAmbulanceById(id);
    }

    public List<HospitalDto> getAllHospitals() {
        return hospitalClient.getAllHospitals();
    }

    public HospitalDto getHospitalById(Long id) {
        return hospitalClient.getHospitalById(id);
    }

//    public NearestAmbulanceDto findNearestAmbulance(double patientLatitude, double patientLongitude, String specialty) {
//        List<AmbulanceDto> availableAmbulances = ambulanceClient.getAllAmbulances()
//                .stream()
//                .filter(a -> a.getStatus() != null && a.getStatus().equalsIgnoreCase("Available"))
//                .collect(Collectors.toList());
//
//        if (availableAmbulances.isEmpty()) {
//            log.info("No available ambulances found.");
//            return null;
//        }
//
//        if (specialty != null && !specialty.isEmpty()) {
//            availableAmbulances = availableAmbulances.stream()
//                    .filter(ambulance -> isAmbulanceSuitableForSpecialty(ambulance, specialty))
//                    .collect(Collectors.toList());
//            log.info("Filtered ambulances for specialty '{}': {}", specialty, availableAmbulances);
//
//            if (availableAmbulances.isEmpty()) {
//                log.info("No available ambulances found with specialty: {}", specialty);
//                return null;
//            }
//        }
//
//        List<AmbulanceWithEta> ambulancesWithEta = new ArrayList<>();
//        boolean usingTrafficApi = false;
//        for (AmbulanceDto ambulance : availableAmbulances) {
//            double eta = getEstimatedArrivalTime(ambulance, patientLatitude, patientLongitude);
//            if (eta > 0){
//                usingTrafficApi = true;
//            }
//            ambulancesWithEta.add(new AmbulanceWithEta(ambulance, eta));
//        }
//
//        ambulancesWithEta.sort(Comparator.comparingDouble(ambulanceWithEta -> ambulanceWithEta.eta));
//
//
//        if (!ambulancesWithEta.isEmpty()) {
//            AmbulanceDto nearestAmbulance = ambulancesWithEta.get(0).ambulanceDto;
//            double distance = calculateDistance(patientLatitude, patientLongitude, nearestAmbulance.getLatitude(), nearestAmbulance.getLongitude());
//            log.info("Nearest Ambulance Found: {}, Distance: {} ", nearestAmbulance.getId(), distance);
//            if (usingTrafficApi){
//                log.info("Nearest ambulance found using TomTom API: {}", nearestAmbulance.getId());
//            }else{
//                log.info("Nearest ambulance found using fallback (distance): {}", nearestAmbulance.getId());
//            }
//            return mapToNearestAmbulanceDto(nearestAmbulance, distance);
//        }
//        return null;
//    }

    public NearestAmbulanceDto findNearestAmbulance(double patientLatitude, double patientLongitude, String specialty) {
        List<AmbulanceDto> availableAmbulances = ambulanceClient.getAllAmbulances()
                .stream()
                .filter(a -> a.getStatus() != null && a.getStatus().equalsIgnoreCase("Available"))
                .collect(Collectors.toList());

        if (availableAmbulances.isEmpty()) {
            log.info("No available ambulances found.");
            return null;
        }

        if (specialty != null && !specialty.isEmpty()) {
            availableAmbulances = availableAmbulances.stream()
                    .filter(ambulance -> isAmbulanceSuitableForSpecialty(ambulance, specialty))
                    .collect(Collectors.toList());

            log.info("Filtered ambulances for specialty '{}': {}", specialty, availableAmbulances);

            if (availableAmbulances.isEmpty()) {
                log.info("No available ambulances found with specialty: {}", specialty);
                return null;
            }
        }

        List<AmbulanceWithEta> ambulancesWithEta = new ArrayList<>();
        boolean usingTrafficApi = false;

        for (AmbulanceDto ambulance : availableAmbulances) {
            double eta = getEstimatedArrivalTime(ambulance, patientLatitude, patientLongitude);
            if (eta > 0){
                usingTrafficApi = true;
            }
            ambulancesWithEta.add(new AmbulanceWithEta(ambulance, eta));
        }

        ambulancesWithEta.sort(Comparator.comparingDouble(ambulanceWithEta -> ambulanceWithEta.eta));

        if (!ambulancesWithEta.isEmpty()) {
            AmbulanceDto nearestAmbulance = ambulancesWithEta.get(0).ambulanceDto;
            double distance = calculateDistance(patientLatitude, patientLongitude, nearestAmbulance.getLatitude(), nearestAmbulance.getLongitude());
            log.info("Nearest Ambulance Found: {}, Distance: {} ", nearestAmbulance.getId(), distance);
            if (usingTrafficApi){
                log.info("Nearest ambulance found using TomTom API: {}", nearestAmbulance.getId());
            }else{
                log.info("Nearest ambulance found using fallback (distance): {}", nearestAmbulance.getId());
            }

            return mapToNearestAmbulanceDto(nearestAmbulance, distance);
        }
        return null;
    }

    private boolean isAmbulanceSuitableForSpecialty(AmbulanceDto ambulance, String specialty){
        if (ambulance.getSpecialty() == null || ambulance.getSpecialty().isEmpty()){
            return false;
        }
        return ambulance.getSpecialty().equalsIgnoreCase(specialty);
    }

//    private boolean isAmbulanceSuitableForSpecialty(AmbulanceDto ambulance, String specialty) {
//        return ambulance.getSpecialty() != null && ambulance.getSpecialty().toLowerCase().contains(specialty.toLowerCase());
//    }

    private double getEstimatedArrivalTime(AmbulanceDto ambulance, double patientLatitude, double patientLongitude) {
        double distance = calculateDistance(ambulance.getLatitude(), ambulance.getLongitude(), patientLatitude, patientLongitude);
        String point = ambulance.getLatitude() + "," + ambulance.getLongitude();

        log.debug("Calculating ETA: Distance is: {} km for ambulance {} at point {}", distance, ambulance.getId(), point);
        log.debug("Attempting to fetch speed from traffic API for ambulance {}", ambulance.getId());
        double speed = getAverageSpeedFromTrafficApi(point);

        if (speed <= 0) {
            log.warn("Could not get speed from traffic API, falling back to simple distance for ambulance {}", ambulance.getId());
            log.debug("ETA Calculation (fallback): distance * 1000 ({} * 1000)", distance);
            return distance;
        }
        log.debug("Speed from traffic API: {} km/h for ambulance {}", speed, ambulance.getId());
        log.debug("ETA Calculation (with traffic): distance * 1000 / (speed / 3.6) : ({} * 1000) /({} / 3.6)", distance, speed);
        return (distance * 1000) / (speed / 3.6);
    }

    private double getAverageSpeedFromTrafficApi(String point) {
        try {
            log.debug("Requesting traffic data for point: {}", point);
            Map<String, Object> flowData = trafficApiClient.getFlowSegmentData(4, "relative0", 12, "json", point, "KMPH", true, apiKey);

            if (flowData != null) {
                log.debug("Traffic API Response: {}", flowData);
                if (flowData.containsKey("flowSegmentData")) {
                    Map<String, Object> flowSegmentData = (Map<String, Object>) flowData.get("flowSegmentData");
                    log.debug("flowSegmentData: {}", flowSegmentData);
                    if (flowSegmentData.containsKey("currentSpeed")) {
                        Integer speedInteger = (Integer) flowSegmentData.get("currentSpeed"); // Get as Integer
                        double speed = speedInteger.doubleValue(); // Convert to double
                        log.debug("Extracted speed: {}", speed);
                        return speed;
                    } else {
                        log.warn("Response does not contain 'currentSpeed' ");
                    }
                } else {
                    log.warn("Response does not contain 'flowSegmentData'");
                }
            } else {
                log.warn("Traffic API response was null");
            }
        } catch (Exception ex) {
            log.error("Error getting speed from traffic API, falling back to simple distance: {}", ex.getMessage());
        }
        return 0.0;
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public NearestAmbulanceDto mapToNearestAmbulanceDto(AmbulanceDto ambulance, double distance) {
        NearestAmbulanceDto dto = new NearestAmbulanceDto();
        dto.setId(ambulance.getId());
        dto.setRegistrationNumber(ambulance.getRegistrationNumber());
        dto.setDistance(distance);
        dto.setLatitude(ambulance.getLatitude());
        dto.setLongitude(ambulance.getLongitude());
        dto.setModel(ambulance.getModel());
        dto.setStatus(ambulance.getStatus());
        dto.setPhone(ambulance.getPhone());
        return dto;
    }

    private record AmbulanceWithEta(AmbulanceDto ambulanceDto, double eta) {
    }
}