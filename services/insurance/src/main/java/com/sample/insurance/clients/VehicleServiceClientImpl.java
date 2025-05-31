package com.sample.insurance.clients;

import com.sample.insurance.model.Vehicle;
import com.sample.insurance.service.VehicleServiceClient;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementation of VehicleServiceClient using WebClient.
 * 
 * Note: This implementation will be replaced by code generated from the OpenAPI spec.
 * This is a placeholder until we set up the generation script as mentioned in the ADR.
 */
@Service
public class VehicleServiceClientImpl implements VehicleServiceClient {

    private final WebClient webClient;
    
    public VehicleServiceClientImpl(
            @Value("${vehicle-service.base-url:http://localhost:50080}") String baseUrl,
            ObservationRegistry observationRegistry) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                // This is crucial for trace propagation - it instruments the WebClient
                .observationRegistry(observationRegistry)
                .build();
    }
    
    @Override
    public Mono<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return webClient.get()
                .uri("/vehicles/{registrationNumber}", registrationNumber)
                .retrieve()
                .bodyToMono(Vehicle.class);
    }
}
