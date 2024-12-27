package com.example.routingservice;

import com.example.routingservice.model.Routing;
import com.example.routingservice.repository.RoutingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.example.ambulanceservice", "com.example.hospitalservice", "com.example.routingservice"})
public class RoutingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoutingDatabase(RoutingRepository repository) {
        return args -> {
            List<Routing> routings = Arrays.asList(
                    new Routing(null, "AmbulanceService", "HospitalService", "{'location': 'near', 'specialty': 'cardiology'}", "Route to nearest hospital with cardiology"),
                    new Routing(null, "PatientService", "AmbulanceService", "{'emergencyLevel': 'high'}", "Route to ambulance for high emergency"),
                    new Routing(null, "HospitalService", "PatientService", "{'status': 'discharge'}", "Update patient record on discharge"),
                    new Routing(null, "AmbulanceService", "PatientService", "{'status': 'pickup'}", "Update patient record on pickup"),
                    new Routing(null, "PatientService", "HospitalService", "{'condition': 'stable'}", "Route to general ward in hospital")
            );

            repository.saveAll(routings);
            System.out.println("5 Routing instances created and saved to the database.");
        };
    }
}