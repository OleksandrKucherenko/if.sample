/*
 * Vehicle Service API
 * API documentation for the Vehicle microservice.
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.sample.insurance.clients.vehicle.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-05-30T13:53:14.574798+02:00[Europe/Stockholm]", comments = "Generator version: 7.13.0")
public interface Authentication {
    /**
     * Apply authentication settings to header and / or query parameters.
     *
     * @param queryParams The query parameters for the request
     * @param headerParams The header parameters for the request
     * @param cookieParams The cookie parameters for the request
     */
    void applyToParams(MultiValueMap<String, String> queryParams, HttpHeaders headerParams, MultiValueMap<String, String> cookieParams);
}
