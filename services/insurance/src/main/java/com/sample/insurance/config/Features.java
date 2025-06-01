package com.sample.insurance.config;

import org.togglz.core.Feature;
import org.togglz.core.activation.GradualActivationStrategy;
import org.togglz.core.annotation.ActivationParameter;
import org.togglz.core.annotation.DefaultActivationStrategy;
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
    @DefaultActivationStrategy(id = LimitedABTestingStrategy.ID, parameters = {
            @ActivationParameter(name = LimitedABTestingStrategy.PARAM_PERCENTAGE, value = "50"),
            @ActivationParameter(name = LimitedABTestingStrategy.PARAM_MAX_ACTIVATIONS, value = "100")
    })
    USE_REDIS_INSURANCE_REPOSITORY,

    @Label("Green/Blue Deployment")
    @DefaultActivationStrategy(id = GradualActivationStrategy.ID, parameters = {
            @ActivationParameter(name = GradualActivationStrategy.PARAM_PERCENTAGE, value = "5") // Start with 5% of traffic
    })
    GREEN_BLUE_DEPLOYMENT;
}
