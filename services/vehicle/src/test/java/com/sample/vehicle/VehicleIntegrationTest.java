package com.sample.vehicle;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VehicleIntegrationTest {
    private final Logger logger = LoggerFactory.getLogger(VehicleIntegrationTest.class);

    @LocalServerPort private int port = 0;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void should_return_vehicle_data() {
        // GIVEN: url and parameter
        var license = "NMG52Y";
        var url = String.format("http://localhost:%s/%s/%s", port, VehicleController.ENTRY, license);

        // WHEN: execute
        var response = restTemplate.getForEntity(url, Vehicle.class);
        logger.info(() -> MessageFormat.format("response: {0}", response.toString()));

        // THEN: verify data
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(license, response.getBody().registrationNumber());
        assertNotNull(response.getBody().id());
    }

    @Test
    public void should_return_json_string() {
        // GIVEN: url and parameter
        var license = "NMG52Y";
        var url = String.format("http://localhost:%s/%s/%s", port, VehicleController.ENTRY, license);

        // WHEN: execute
        var response = restTemplate.getForEntity(url, String.class);
        logger.info(() -> MessageFormat.format("response body: {0}", response.getBody()));

        // THEN: nbe sure we have OK
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
