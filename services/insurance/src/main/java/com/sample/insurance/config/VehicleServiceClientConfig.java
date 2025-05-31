package com.sample.insurance.config;

import com.sample.insurance.clients.VehicleServiceClientImpl;
import com.sample.insurance.service.FeatureToggleVehicleServiceClient;
import com.sample.insurance.service.VehicleServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for Vehicle Service Client implementation.
 * Provides beans for the feature toggle-enabled vehicle service client.
 */
@Configuration
public class VehicleServiceClientConfig {

    /**
     * Configure the primary VehicleServiceClient to be used by the application.
     * This ensures that the feature toggle-enabled client is injected when a
     * VehicleServiceClient is requested.
     *
     * @param client The regular vehicle service client
     * @param featureToggleClient The feature toggle-enabled vehicle service client
     * @return The primary VehicleServiceClient bean
     */
    @Bean
    @Primary
    public VehicleServiceClient primaryVehicleServiceClient(
            VehicleServiceClientImpl client, 
            FeatureToggleVehicleServiceClient featureToggleClient) {
        return featureToggleClient;
    }
}
