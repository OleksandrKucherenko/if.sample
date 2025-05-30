package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * Vehicle entity representing data returned from Vehicle Service.
 */
@Schema(description = "Vehicle entity representing a car or truck")
public record Vehicle(
    @Schema(description = "Unique database identifier")
    UUID id,
    
    @Schema(description = "Registration number of the vehicle")
    String registrationNumber
) {
    /** Instead of NULL we can use EMPTY object. */
    public static final Vehicle EMPTY = new Vehicle(new UUID(0L, 0L), "");
}
