package com.example.routingservice.dto;

import lombok.Data;

@Data
public class AmbulanceRoutingDto {
    private Long id;
    private String registrationNumber;
    private String model;
    private String status;
    private double latitude;
    private double longitude;
}