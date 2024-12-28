package com.example.ambulanceservice.controller;

import com.example.ambulanceservice.model.Ambulance;
import com.example.ambulanceservice.service.AmbulanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/ambulances")
public class AmbulanceController {

    @Autowired
    private AmbulanceService ambulanceService;

    @GetMapping
    public List<Ambulance> getAllAmbulances(){
        return ambulanceService.getAllAmbulances();
    }

    @GetMapping("/{id}")
    public Ambulance getAmbulanceById(@PathVariable Long id) {
        return ambulanceService.getAmbulanceById(id);
    }

    @PostMapping
    public Ambulance createAmbulance(@RequestBody Ambulance ambulance) {
        return ambulanceService.createAmbulance(ambulance);
    }

    @PutMapping("/{id}")
    public Ambulance updateAmbulance(@PathVariable Long id, @RequestBody Ambulance ambulance) {
        return ambulanceService.updateAmbulance(id, ambulance);
    }

    @PutMapping("/{id}/location")
    public Ambulance updateAmbulanceLocation(@PathVariable Long id,
                                             @RequestParam double latitude,
                                             @RequestParam double longitude) {
        return ambulanceService.updateAmbulanceLocation(id, latitude, longitude);
    }

    @DeleteMapping("/{id}")
    public void deleteAmbulance(@PathVariable Long id){
        ambulanceService.deleteAmbulance(id);
    }
}