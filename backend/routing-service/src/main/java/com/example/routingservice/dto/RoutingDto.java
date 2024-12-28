package com.example.routingservice.dto;

import lombok.Data;

@Data
public class RoutingDto {
    private Long id;
    private String fromService;
    private String toService;
    private String criteria;
    private String decision;
}