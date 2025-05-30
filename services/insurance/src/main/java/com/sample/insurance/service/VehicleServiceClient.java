package com.sample.insurance.service;

import com.sample.insurance.model.Vehicle;
import reactor.core.publisher.Mono;

/**
 * Client interface for communicating with the Vehicle Service.
 */
public interface VehicleServiceClient {
    
    /**
     * Retrieves vehicle information by its registration number.
     *
     * @param registrationNumber The vehicle registration number
     * @return Vehicle information
     * @throws RuntimeException if the vehicle information cannot be retrieved
     */
    Mono<Vehicle> getVehicleByRegistrationNumber(String registrationNumber);
}
