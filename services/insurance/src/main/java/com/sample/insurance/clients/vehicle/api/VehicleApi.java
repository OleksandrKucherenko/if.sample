package com.sample.insurance.clients.vehicle.api;

import com.sample.insurance.clients.vehicle.ApiClient;

import com.sample.insurance.clients.vehicle.model.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-05-30T13:53:14.574798+02:00[Europe/Stockholm]", comments = "Generator version: 7.13.0")
public class VehicleApi {
    private ApiClient apiClient;

    public VehicleApi() {
        this(new ApiClient());
    }

    @Autowired
    public VehicleApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Get vehicle by registration number
     * 
     * <p><b>200</b> - OK
     * @param registrationNumber The registrationNumber parameter
     * @return Vehicle
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getVehicleRequestCreation(@jakarta.annotation.Nonnull String registrationNumber) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'registrationNumber' is set
        if (registrationNumber == null) {
            throw new WebClientResponseException("Missing the required parameter 'registrationNumber' when calling getVehicle", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("registrationNumber", registrationNumber);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<Vehicle> localVarReturnType = new ParameterizedTypeReference<Vehicle>() {};
        return apiClient.invokeAPI("/vehicles/{registrationNumber}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get vehicle by registration number
     * 
     * <p><b>200</b> - OK
     * @param registrationNumber The registrationNumber parameter
     * @return Vehicle
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Vehicle> getVehicle(@jakarta.annotation.Nonnull String registrationNumber) throws WebClientResponseException {
        ParameterizedTypeReference<Vehicle> localVarReturnType = new ParameterizedTypeReference<Vehicle>() {};
        return getVehicleRequestCreation(registrationNumber).bodyToMono(localVarReturnType);
    }

    /**
     * Get vehicle by registration number
     * 
     * <p><b>200</b> - OK
     * @param registrationNumber The registrationNumber parameter
     * @return ResponseEntity&lt;Vehicle&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Vehicle>> getVehicleWithHttpInfo(@jakarta.annotation.Nonnull String registrationNumber) throws WebClientResponseException {
        ParameterizedTypeReference<Vehicle> localVarReturnType = new ParameterizedTypeReference<Vehicle>() {};
        return getVehicleRequestCreation(registrationNumber).toEntity(localVarReturnType);
    }

    /**
     * Get vehicle by registration number
     * 
     * <p><b>200</b> - OK
     * @param registrationNumber The registrationNumber parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getVehicleWithResponseSpec(@jakarta.annotation.Nonnull String registrationNumber) throws WebClientResponseException {
        return getVehicleRequestCreation(registrationNumber);
    }

    /**
     * Ping vehicle service
     * 
     * <p><b>200</b> - OK
     * @return String
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec pingRequestCreation() throws WebClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = { 
            "*/*"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<String> localVarReturnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI("/vehicles/ping", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Ping vehicle service
     * 
     * <p><b>200</b> - OK
     * @return String
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<String> ping() throws WebClientResponseException {
        ParameterizedTypeReference<String> localVarReturnType = new ParameterizedTypeReference<String>() {};
        return pingRequestCreation().bodyToMono(localVarReturnType);
    }

    /**
     * Ping vehicle service
     * 
     * <p><b>200</b> - OK
     * @return ResponseEntity&lt;String&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<String>> pingWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<String> localVarReturnType = new ParameterizedTypeReference<String>() {};
        return pingRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Ping vehicle service
     * 
     * <p><b>200</b> - OK
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec pingWithResponseSpec() throws WebClientResponseException {
        return pingRequestCreation();
    }
}
