package com.example.routingservice.client;

import com.example.common.dto.AmbulanceDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ambulance-service")
public interface AmbulanceClient {
    @GetMapping("/ambulances")
    List<AmbulanceDto> getAllAmbulances();
    @GetMapping("/ambulances/{id}")
    AmbulanceDto getAmbulanceById(@PathVariable Long id);
}