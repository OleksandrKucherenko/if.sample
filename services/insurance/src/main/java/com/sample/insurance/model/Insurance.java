package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * Base insurance class representing any type of insurance.
 */
@Schema(description = "Base insurance entity")
public abstract class Insurance {
    
    @Schema(description = "Unique identifier of the insurance")
    private final UUID id;
    
    @Schema(description = "Type of insurance")
    private final String type;
    
    @Schema(description = "Monthly cost of the insurance")
    private final Money monthlyCost;
    
    @Schema(description = "Personal identification number of the insured person")
    private final String personalIdNumber;
    
    protected Insurance(UUID id, String type, Money monthlyCost, String personalIdNumber) {
        this.id = id;
        this.type = type;
        this.monthlyCost = monthlyCost;
        this.personalIdNumber = personalIdNumber;
    }
    
    public UUID getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }
    
    public Money getMonthlyCost() {
        return monthlyCost;
    }
    
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }
}
