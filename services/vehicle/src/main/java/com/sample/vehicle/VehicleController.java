package com.sample.vehicle;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Vehicle", description = "Vehicle management")
@RestController
@RequestMapping(VehicleController.ENTRY)
public class VehicleController {
    public static final String ENTRY = "/vehicles";

    @GetMapping("/ping")
    @Operation(summary = "Ping vehicle service")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @GetMapping("/{registrationNumber}")
    @Operation(summary = "Get vehicle by registration number")
    public Vehicle getVehicle(@PathVariable String registrationNumber) {
        return new Vehicle(
                UUID.randomUUID(),
                registrationNumber
        );
    }
}
