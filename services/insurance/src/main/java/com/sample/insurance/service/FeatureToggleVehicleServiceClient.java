package com.sample.insurance.service;

import com.sample.insurance.clients.VehicleServiceClientImpl;
import com.sample.insurance.model.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * A wrapper around the VehicleServiceClient that uses feature toggle to
 * enable/disable calls to the vehicle service.
 */
@Service
public class FeatureToggleVehicleServiceClient implements VehicleServiceClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VehicleServiceClientImpl vehicleServiceClient;
    private final FeatureToggleService featureToggleService;

    public FeatureToggleVehicleServiceClient(
            VehicleServiceClientImpl vehicleServiceClient,
            FeatureToggleService featureToggleService) {
        this.vehicleServiceClient = vehicleServiceClient;
        this.featureToggleService = featureToggleService;
    }

    /**
     * Get vehicle by registration number, if the USE_VEHICLE_SERVICE feature is
     * enabled.
     * If the feature is disabled, returns an empty Mono.
     *
     * @param registrationNumber Vehicle registration number
     * @return Vehicle information or empty if feature is disabled
     */
    @Override
    public Mono<Vehicle> getVehicleByRegistrationNumber(String registrationNumber) {
        if (featureToggleService.shouldUseVehicleService()) {
            return vehicleServiceClient.getVehicleByRegistrationNumber(registrationNumber);
        }

        logger.info("Vehicle service call disabled by feature toggle for: {}", registrationNumber);
        return Mono.empty();
    }
}
