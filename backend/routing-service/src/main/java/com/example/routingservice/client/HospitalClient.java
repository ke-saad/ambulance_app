package com.example.routingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.common.dto.HospitalDto;

import java.util.List;

@FeignClient(name = "hospital-service")
public interface HospitalClient {
    @GetMapping("/hospitals")
    List<HospitalDto> getAllHospitals();

    @GetMapping("/hospitals/{id}")
    HospitalDto getHospitalById(@PathVariable Long id);
}