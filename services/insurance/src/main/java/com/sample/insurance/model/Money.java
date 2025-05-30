package com.sample.insurance.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Money value object that stores amount and currency.
 * Amount is stored as integer for performance reasons, e.g., 100 for 1.00 SEK.
 */
@Schema(description = "Money representation with amount and currency")
public record Money(
    @Schema(description = "Amount in the smallest unit of the currency (e.g., 100 for 1.00)")
    int amount,
    
    @Schema(description = "Currency code according to ISO 4217 (e.g., SEK, USD)")
    String currency
) {
    public static Money of(int amount, String currency) {
        return new Money(amount, currency);
    }
    
    /**
     * Creates a new Money object with USD currency
     */
    public static Money usd(int amount) {
        return new Money(amount, "USD");
    }
}
