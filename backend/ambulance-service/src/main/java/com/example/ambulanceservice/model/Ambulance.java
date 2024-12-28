package com.example.ambulanceservice.model;

import com.example.ambulanceservice.model.enums.AmbulanceStatus;
import com.example.ambulanceservice.model.enums.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ambulances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNumber;

    private String model;

    @Enumerated(EnumType.STRING)
    private AmbulanceStatus status;

    private double latitude;

    private double longitude;

    private Specialty specialty;

    private String phone;

}