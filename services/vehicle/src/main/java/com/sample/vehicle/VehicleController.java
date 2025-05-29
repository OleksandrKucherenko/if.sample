package com.sample.vehicle;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@RestController
@RequestMapping(VehicleController.ENTRY)
public class VehicleController {
    public static final String ENTRY = "/vehicles";

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/{registrationNumber}")
    public Vehicle getVehicle(@PathVariable String registrationNumber) {
        return new Vehicle(
                UUID.randomUUID(),
                registrationNumber
        );
    }
}
