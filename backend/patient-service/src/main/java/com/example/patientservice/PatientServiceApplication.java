package com.example.patientservice;

import com.example.patientservice.model.Patient;
import com.example.patientservice.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class PatientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initPatientDatabase(PatientRepository repository) {
        return args -> {
            List<Patient> patients = Arrays.asList(
                    new Patient(null, "John", "Doe", "Male", "1980-05-15", "555-111-2222", "123 Main St, Anytown"),
                    new Patient(null, "Jane", "Smith", "Female", "1992-11-02", "555-333-4444", "456 Oak Ave, Springfield"),
                    new Patient(null, "David", "Lee", "Male", "1975-03-20", "555-555-6666", "789 Pine Ln, Riverside"),
                    new Patient(null, "Sarah", "Jones", "Female", "1988-09-10", "555-777-8888", "101 University Dr, Midtown"),
                    new Patient(null, "Michael", "Brown", "Male", "2000-01-25", "555-999-0000", "222 Hope Ln, New Hope")
            );

            repository.saveAll(patients);
            System.out.println("5 Patient instances created and saved to the database.");
        };
    }
}