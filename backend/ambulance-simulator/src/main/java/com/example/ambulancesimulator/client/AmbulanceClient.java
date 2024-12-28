package com.example.ambulancesimulator.client;

import com.example.common.dto.AmbulanceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "ambulance-service")
public interface AmbulanceClient {
    @GetMapping("/ambulances")
    List<AmbulanceDto> getAllAmbulances();
    @GetMapping("/ambulances/{id}")
    AmbulanceDto getAmbulanceById(@PathVariable Long id);
    @PostMapping("/ambulances")
    AmbulanceDto createAmbulance(@RequestBody AmbulanceDto ambulance);
    @PutMapping("/ambulances/{id}")
    AmbulanceDto updateAmbulance(@PathVariable Long id, @RequestBody AmbulanceDto ambulance);
    @PutMapping("/ambulances/{id}/location")
    AmbulanceDto updateAmbulanceLocation(@PathVariable Long id, @RequestParam double latitude, @RequestParam double longitude);
}