package com.sample.insurance.controller;

import com.sample.insurance.config.Features;
import com.sample.insurance.service.FeatureToggleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for feature toggle related operations.
 * Provides endpoints to check the status of feature toggles.
 */
@RestController
@RequestMapping("/api/features")
public class FeatureToggleController {

    private final FeatureToggleService featureToggleService;
    private final FeatureManager featureManager;

    public FeatureToggleController(FeatureToggleService featureToggleService, FeatureManager featureManager) {
        this.featureToggleService = featureToggleService;
        this.featureManager = featureManager;
    }

    /**
     * Get the status of all feature toggles.
     *
     * @return Map of feature names to their status
     */
    @GetMapping
    public ResponseEntity<Map<String, Boolean>> getFeatureToggles() {
        Map<String, Boolean> features = new HashMap<>();
        
        // Add each feature and its status to the map
        features.put("USE_VEHICLE_SERVICE", featureToggleService.shouldUseVehicleService());
        features.put("USE_REDIS_INSURANCE_REPOSITORY", featureToggleService.shouldUseRedisInsuranceRepository());
        features.put("GREEN_BLUE_DEPLOYMENT", featureToggleService.isGreenBlueDeploymentEnabled());
        
        return ResponseEntity.ok(features);
    }

    /**
     * Get the status of the green/blue deployment feature toggle.
     * This endpoint can be used by CI/CD pipelines to check if a new deployment
     * should be activated.
     *
     * @return Status of the green/blue deployment feature
     */
    @GetMapping("/deployment")
    public ResponseEntity<Map<String, Object>> getDeploymentStatus() {
        boolean isGreenBlue = featureManager.isActive(Features.GREEN_BLUE_DEPLOYMENT);
        
        Map<String, Object> response = new HashMap<>();
        response.put("feature", "GREEN_BLUE_DEPLOYMENT");
        response.put("enabled", isGreenBlue);
        response.put("deploymentType", isGreenBlue ? "BLUE" : "GREEN");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
