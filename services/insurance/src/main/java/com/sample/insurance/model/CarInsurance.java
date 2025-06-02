package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Car insurance product with fixed monthly cost of $30.
 * This insurance type is linked to a vehicle in the Vehicle Service.
 */
@Schema(description = "Car insurance entity")
public class CarInsurance extends Insurance {
    
    @Schema(description = "Registration number of the insured vehicle")
    private final String registrationNumber;
    
    @Schema(description = "Vehicle information from Vehicle Service")
    private Vehicle vehicle = null;
    
    @JsonCreator
    public CarInsurance(
            @JsonProperty("id") UUID id, 
            @JsonProperty("personalIdNumber") String personalIdNumber, 
            @JsonProperty("registrationNumber") String registrationNumber) {
        super(id, "CAR", Money.usd(3000), personalIdNumber); // $30.00
        this.registrationNumber = registrationNumber;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public CarInsurance setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public static CarInsurance from(String personaId, String registrationNumber) {
        return new CarInsurance(UUID.randomUUID(), personaId, registrationNumber);
    }
}
