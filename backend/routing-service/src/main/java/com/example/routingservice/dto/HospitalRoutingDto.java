package com.example.routingservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class HospitalRoutingDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private double latitude;
    private double longitude;
    private List<String> specialties;
}