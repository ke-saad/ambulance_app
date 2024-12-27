package com.example.routingservice.controller;

import com.example.common.dto.AmbulanceDto;
import com.example.common.dto.HospitalDto;
import com.example.routingservice.dto.NearestAmbulanceDto;
import com.example.routingservice.model.Routing;
import com.example.routingservice.service.RoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routings")
public class RoutingController {

    private static final Logger log = LoggerFactory.getLogger(RoutingController.class);
    @Autowired
    private RoutingService routingService;

    @GetMapping
    public List<Routing> getAllRoutings() {
        return routingService.getAllRoutings();
    }

    @GetMapping("/{id}")
    public Routing getRoutingById(@PathVariable Long id) {
        return routingService.getRoutingById(id);
    }

    @PostMapping
    public Routing createRouting(@RequestBody Routing routing) {
        return routingService.createRouting(routing);
    }

    @PutMapping("/{id}")
    public Routing updateRouting(@PathVariable Long id, @RequestBody Routing updatedRouting) {
        return routingService.updateRouting(id, updatedRouting);
    }

    @DeleteMapping("/{id}")
    public void deleteRouting(@PathVariable Long id) {
        routingService.deleteRouting(id);
    }

    @GetMapping("/route/ambulances")
    public List<AmbulanceDto> getAllAmbulances() {
        return routingService.getAllAmbulances();
    }

    @GetMapping("/route/ambulances/{id}")
    public AmbulanceDto getAmbulanceById(@PathVariable Long id) {
        return routingService.getAmbulanceById(id);
    }

    @GetMapping("/route/hospitals")
    public List<HospitalDto> getAllHospitals() {
        return routingService.getAllHospitals();
    }

    @GetMapping("/route/hospitals/{id}")
    public HospitalDto getHospitalById(@PathVariable Long id) {
        return routingService.getHospitalById(id);
    }

    @GetMapping("/nearest-ambulance")
    public ResponseEntity<NearestAmbulanceDto> findNearestAmbulance(
            @RequestParam double patientLatitude,
            @RequestParam double patientLongitude,
            @RequestParam(required = false) String specialty) {

        log.info("Received request with params - Latitude: {}, Longitude: {}, Specialty: {}", patientLatitude, patientLongitude, specialty);

        NearestAmbulanceDto nearestAmbulanceDto = routingService.findNearestAmbulance(patientLatitude, patientLongitude, specialty);

        if (nearestAmbulanceDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nearestAmbulanceDto);
    }

}