package com.sample.insurance.config;

import org.togglz.core.Feature;
import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.annotation.Label;

/**
 * Feature toggles used in the application.
 * <p>
 * USE_VEHICLE_SERVICE - Enables/disables the call to vehicle service
 * USE_REDIS_INSURANCE_REPOSITORY - A/B testing between MockInsuranceRepository
 * and RedisInsuranceRepository
 * GREEN_BLUE_DEPLOYMENT - Used by CI/CD for green/blue deployments
 */
public enum Features implements Feature {

    @EnabledByDefault
    @Label("Use Vehicle Service")
    USE_VEHICLE_SERVICE,

    @Label("Use Redis Insurance Repository")
    USE_REDIS_INSURANCE_REPOSITORY,

    @Label("Green/Blue Deployment")
    GREEN_BLUE_DEPLOYMENT;
}
