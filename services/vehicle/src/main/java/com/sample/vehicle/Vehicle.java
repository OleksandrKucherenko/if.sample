package com.sample.vehicle;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Vehicle entity representing a car or truck")
public record Vehicle(
    @Schema(description = "Unique database identifier")
    UUID id,
    @Schema(description = "Registration number of the vehicle")
    String registrationNumber
) {}