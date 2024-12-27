package com.example.hospitalservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String email;

    private double latitude;
    private double longitude;

    @ElementCollection
    @CollectionTable(name = "hospital_specialties", joinColumns = @JoinColumn(name = "hospital_id"))
    @Column(name = "specialty")
    private List<String> specialties;
}