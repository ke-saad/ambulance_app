package com.example.common.dto;

import lombok.Data;

@Data
public class AmbulanceDto {

    private Long id;
    private String registrationNumber;
    private String model;
    private String status;
    private double latitude;
    private double longitude;
    private String specialty;
    private String phone;
}