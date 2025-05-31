package com.sample.insurance.clients;

import com.sample.insurance.clients.vehicle.ApiClient;
import com.sample.insurance.clients.vehicle.api.VehicleApi;
import com.sample.insurance.model.Vehicle;
import com.sample.insurance.service.VehicleServiceClient;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Implementation of VehicleServiceClient using WebClient.
 * <p>
 * Note: This implementation will be replaced by code generated from the OpenAPI spec.
 * This is a placeholder until we set up the generation script as mentioned in the ADR.
 */
@Service
public class VehicleServiceClientImpl implements VehicleServiceClient {
    private final VehicleApi api;

    public VehicleServiceClientImpl(
            @Value("${vehicle-service.base-url:http://localhost:50080}") String baseUrl,
            ObservationRegistry observationRegistry) {

        // This is crucial for trace propagation - it instruments the WebClient
        var webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .observationRegistry(observationRegistry)
                .build();

        this.api = new VehicleApi(new ApiClient(webClient));
    }

    @Override
    public Mono<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        return api.getVehicle(registrationNumber).map(this::convertToDomainVehicle);
    }

    private Vehicle convertToDomainVehicle(com.sample.insurance.clients.vehicle.model.Vehicle vehicle) {
        // TODO (olku): that can be extracted later to converters abstraction, if needed
        //   Or as alternative, we can promote API vehicle entity to Service domain entity
        return new Vehicle(vehicle.getId(), vehicle.getRegistrationNumber());
    }
}
