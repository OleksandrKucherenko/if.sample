package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Person entity containing identification and personal information.
 */
@Schema(description = "Person entity with personal identification details")
public record Person(
    @Schema(description = "Personal identification number (e.g., 920328-4428)")
    String personalIdNumber,
    
    @Schema(description = "Person's full name")
    String name,
    
    @Schema(description = "Street address")
    String street,
    
    @Schema(description = "Postal address")
    String postalAddress,
    
    @Schema(description = "Phone number")
    String phone,
    
    @Schema(description = "Email address")
    String email
) {
}
