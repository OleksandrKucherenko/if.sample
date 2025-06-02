package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pet insurance product with fixed monthly cost of $10.
 */
@Schema(description = "Pet insurance entity")
public class PetInsurance extends Insurance {

    @Schema(description = "Type of pet")
    private final String petType;

    @Schema(description = "Pet's name")
    private final String petName;

    @JsonCreator
    public PetInsurance(
            @JsonProperty("id") UUID id,
            @JsonProperty("personalIdNumber") String personalIdNumber,
            @JsonProperty("petType") String petType,
            @JsonProperty("petName") String petName) {
        super(id, "PET", Money.usd(1000), personalIdNumber); // $10.00
        this.petType = petType;
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public String getPetName() {
        return petName;
    }

    public static PetInsurance from(String personaId, String petType, String petName) {
        return new PetInsurance(UUID.randomUUID(), personaId, petType, petName);
    }
}
