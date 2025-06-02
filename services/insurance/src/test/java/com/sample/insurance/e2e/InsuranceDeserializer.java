package com.sample.insurance.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.insurance.model.CarInsurance;
import com.sample.insurance.model.HealthInsurance;
import com.sample.insurance.model.Insurance;
import com.sample.insurance.model.PetInsurance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

/**
 * Helper class to configure JSON deserialization for abstract Insurance class
 */
public class InsuranceDeserializer {
    public static final Logger logger = LoggerFactory.getLogger(InsuranceDeserializer.class);

    private static final ParameterizedTypeReference<List<Insurance>> typeDef = new ParameterizedTypeReference<>() {
    };

    private static final HttpHeaders JSON_HEADERS = new HttpHeaders();

    static {
        JSON_HEADERS.setAccept(List.of(APPLICATION_JSON));
    }

    /**
     * Creates a configured TestRestTemplate that can handle the Insurance class hierarchy
     *
     * @return Configured TestRestTemplate
     */
    @NotNull
    public static RestTemplate createRestTemplateForInsurance() {
        var converter = createInsuranceConverter();

        // Create a RestTemplate with our converter as the only converter
        var restTemplate = new RestTemplate(List.of(converter));

        // Set up a custom error handler that doesn't throw exceptions on 4xx/5xx
        restTemplate.setErrorHandler(response -> {
            logger.info("hasError: {}", response.getStatusText());
            return false;
        });

        return restTemplate;
    }

    @NotNull
    private static MappingJackson2HttpMessageConverter createInsuranceConverter() {
        // Create a custom object mapper that knows about our Insurance subtypes
        var objectMapper = new ObjectMapper();

        objectMapper.registerSubtypes(
                CarInsurance.class,
                HealthInsurance.class,
                PetInsurance.class
        );

        // Create a custom Jackson converter
        var converter = new MappingJackson2HttpMessageConverter(objectMapper);

        // Support all media types to ensure our converter is used
        converter.setSupportedMediaTypes(List.of(APPLICATION_JSON, APPLICATION_OCTET_STREAM));

        return converter;
    }

    /**
     * A utility method to perform a GET request and parse the response as a list of Insurance objects
     *
     * @param restTemplate The TestRestTemplate to use
     * @param url          The URL to request
     * @return The response entity containing a list of Insurance objects
     */
    @NotNull
    public static ResponseEntity<List<Insurance>> getInsuranceList(@NotNull RestTemplate restTemplate, @NotNull String url) {
        return restTemplate.exchange(url, GET, new HttpEntity<>(JSON_HEADERS), typeDef);
    }
}
