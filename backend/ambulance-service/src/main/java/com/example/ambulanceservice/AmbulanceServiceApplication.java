package com.example.ambulanceservice;

import com.example.ambulanceservice.model.Ambulance;
import com.example.ambulanceservice.model.enums.AmbulanceStatus;
import com.example.ambulanceservice.model.enums.Specialty;
import com.example.ambulanceservice.repository.AmbulanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class AmbulanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmbulanceServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(AmbulanceRepository repository) {
        return args -> {
            List<Ambulance> ambulances = Arrays.asList(
                    new Ambulance(null, "KA-01-AB-1234", "Model X", AmbulanceStatus.Available, 34.0522, -118.2437, Specialty.General, "555-111-1111"),
                    new Ambulance(null, "MH-02-CD-5678", "Model Y", AmbulanceStatus.Available, 37.7749, -122.4194, Specialty.Cardiology, "555-222-2222"),
                    new Ambulance(null, "TN-03-EF-9012", "Model Z", AmbulanceStatus.Available, 33.7489, -117.8455, Specialty.Oncology, "555-333-3333"),
                    new Ambulance(null, "GJ-04-GH-3456", "Model X", AmbulanceStatus.Available, 40.7128, -74.0060, Specialty.Pediatrics, "555-444-4444"),
                    new Ambulance(null, "DL-05-IJ-7890", "Model Y", AmbulanceStatus.Available, 39.9526, -75.1652, Specialty.Trauma, "555-555-5555")
            );

            repository.saveAll(ambulances);
            System.out.println("5 Ambulance instances created and saved to the database.");
        };
    }
}