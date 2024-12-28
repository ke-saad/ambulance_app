package com.example.ambulancesimulator;

import com.example.ambulancesimulator.client.AmbulanceClient;
import com.example.common.dto.AmbulanceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@ConditionalOnProperty(name = "simulator.enabled", havingValue = "true")
public class AmbulanceSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmbulanceSimulatorApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AmbulanceClient ambulanceClient;

    private static final double MIN_LAT = -90.0; // Example minimum latitude
    private static final double MAX_LAT = 90.0;  // Example maximum latitude
    private static final double MIN_LON = -180.0; // Example minimum longitude
    private static final double MAX_LON = 180.0; // Example maximum longitude

    @Scheduled(fixedRate = 5000) // Run every 5 seconds (adjust as needed)
    public void simulateAmbulanceMovement() {
        try {
            System.out.println("Simulating ambulance movement...");

            // Fetch all ambulances
            List<AmbulanceDto> ambulances = ambulanceClient.getAllAmbulances();
            if (ambulances == null || ambulances.isEmpty()) {
                System.out.println("No ambulances found to simulate.");
                return;
            }

            // Update each ambulance
            for (AmbulanceDto ambulance : ambulances) {
                // Generate random latitude and longitude within a range
                double newLatitude = ambulance.getLatitude() + ThreadLocalRandom.current().nextDouble(-0.01, 0.01);
                double newLongitude = ambulance.getLongitude() + ThreadLocalRandom.current().nextDouble(-0.01, 0.01);

                // Ensure the new coordinates are within valid bounds
                newLatitude = Math.max(MIN_LAT, Math.min(MAX_LAT, newLatitude));
                newLongitude = Math.max(MIN_LON, Math.min(MAX_LON, newLongitude));

                // Update the ambulance location
                ambulanceClient.updateAmbulanceLocation(ambulance.getId(), newLatitude, newLongitude);
                System.out.println("Updated location for ambulance ID " + ambulance.getId() + ": Lat = " + newLatitude + ", Lon = " + newLongitude);
            }

            System.out.println("Ambulance movement simulation complete.");
        } catch (Exception e) {
            System.err.println("Error during ambulance simulation: " + e.getMessage());
            e.printStackTrace();
        }
    }

}