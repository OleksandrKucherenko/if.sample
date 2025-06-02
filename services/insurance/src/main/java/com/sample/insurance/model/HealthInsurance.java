package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Personal health insurance product with fixed monthly cost of $20.
 */
@Schema(description = "Health insurance entity")
public class HealthInsurance extends Insurance {
    
    @Schema(description = "Coverage level")
    private final String coverageLevel;
    
    @JsonCreator
    public HealthInsurance(
            @JsonProperty("id") UUID id, 
            @JsonProperty("personalIdNumber") String personalIdNumber, 
            @JsonProperty("coverageLevel") String coverageLevel) {
        super(id, "HEALTH", Money.usd(2000), personalIdNumber); // $20.00
        this.coverageLevel = coverageLevel;
    }
    
    public String getCoverageLevel() {
        return coverageLevel;
    }

    public static HealthInsurance from(String personaId, String coverageLevel) {
        return new HealthInsurance(UUID.randomUUID(), personaId, coverageLevel);
    }
}
