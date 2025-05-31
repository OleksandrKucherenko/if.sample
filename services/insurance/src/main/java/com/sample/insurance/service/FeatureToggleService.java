package com.sample.insurance.service;

import com.sample.insurance.config.Features;
import org.springframework.stereotype.Service;
import org.togglz.core.manager.FeatureManager;

/**
 * Service for handling feature toggle operations.
 * This service provides methods to check if features are enabled/disabled.
 */
@Service
public class FeatureToggleService {

    private final FeatureManager featureManager;

    public FeatureToggleService(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    /**
     * Check if the vehicle service should be used.
     *
     * @return true if vehicle service should be used, false otherwise
     */
    public boolean shouldUseVehicleService() {
        return featureManager.isActive(Features.USE_VEHICLE_SERVICE);
    }

    /**
     * Check if the Redis insurance repository should be used (Redis implementation).
     * Used for A/B testing between MockInsuranceRepository and RedisInsuranceRepository.
     *
     * @return true if Redis repository should be used, false for mock repository
     */
    public boolean shouldUseRedisInsuranceRepository() {
        return featureManager.isActive(Features.USE_REDIS_INSURANCE_REPOSITORY);
    }

    /**
     * Check if the green/blue deployment feature is enabled.
     *
     * @return true if green/blue deployment is active, false otherwise
     */
    public boolean isGreenBlueDeploymentEnabled() {
        return featureManager.isActive(Features.GREEN_BLUE_DEPLOYMENT);
    }
}
