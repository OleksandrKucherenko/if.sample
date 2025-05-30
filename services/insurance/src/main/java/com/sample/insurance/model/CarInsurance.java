package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * Car insurance product with fixed monthly cost of $30.
 * This insurance type is linked to a vehicle in the Vehicle Service.
 */
@Schema(description = "Car insurance entity")
public class CarInsurance extends Insurance {
    
    @Schema(description = "Registration number of the insured vehicle")
    private final String registrationNumber;
    
    @Schema(description = "Vehicle information from Vehicle Service")
    private Vehicle vehicle;
    
    public CarInsurance(UUID id, String personalIdNumber, String registrationNumber) {
        super(id, "CAR", Money.usd(3000), personalIdNumber); // $30.00
        this.registrationNumber = registrationNumber;
    }
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }
    
    public Vehicle getVehicle() {
        return vehicle;
    }
    
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
