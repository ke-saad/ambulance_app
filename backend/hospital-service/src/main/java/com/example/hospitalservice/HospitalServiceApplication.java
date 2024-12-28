package com.example.hospitalservice;

import com.example.hospitalservice.model.Hospital;
import com.example.hospitalservice.repository.HospitalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class HospitalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initHospitalDatabase(HospitalRepository repository) {
        return args -> {
            List<Hospital> hospitals = Arrays.asList(
                    new Hospital(null, "City General Hospital", "123 Main St, Anytown", "555-123-4567", "info@citygeneral.com", 34.0522, -118.2437, Arrays.asList("Cardiology", "Emergency")),
                    new Hospital(null, "St. Mary's Medical Center", "456 Oak Ave, Springfield", "555-987-6543", "contact@stmarys.org", 37.7749, -122.4194, Arrays.asList("Oncology", "Pediatrics")),
                    new Hospital(null, "County Hospital", "789 Pine Ln, Riverside", "555-246-8013", "admin@countyhospital.net", 33.7489, -117.8455, Arrays.asList("General Surgery", "Emergency")),
                    new Hospital(null, "University Medical Center", "101 University Dr, Midtown", "555-135-7924", "info@unimedcenter.edu", 40.7128, -74.0060, Arrays.asList("Neurology", "Research")),
                    new Hospital(null, "Hopewell Clinic", "222 Hope Ln, New Hope", "555-864-2097", "reception@hopewellclinic.com", 39.9526, -75.1652, Arrays.asList("Pediatrics", "Family Medicine"))
            );

            repository.saveAll(hospitals);
            System.out.println("5 Hospital instances created and saved to the database.");
        };
    }
}