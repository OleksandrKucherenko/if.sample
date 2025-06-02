package com.sample.insurance.e2e;

import com.sample.insurance.config.Features;
import com.sample.insurance.controller.InsuranceController;
import com.sample.insurance.repository.MockInsuranceRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

import static java.text.MessageFormat.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
public class E2eTestContainersDemoTest {
    private final static Logger logger = LoggerFactory.getLogger(E2eTestContainersDemoTest.class);

    @LocalServerPort
    private int port = 0;

    private static final int REDIS_PORT = 6379;

    @SuppressWarnings("resource")
    @Container
    private static final GenericContainer<?> redis = new GenericContainer<>("redis:8-alpine")
            .withExposedPorts(REDIS_PORT);

    @Autowired
    private FeatureManager featureManager;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redis::getHost);
        registry.add("redis.port", redis::getFirstMappedPort);

        logger.info(() -> format("redis.host: {0}", redis.getHost()));
        logger.info(() -> format("redis.port: {0}", redis.getFirstMappedPort()));
    }

    @BeforeAll
    static void beforeAll() {
        redis.start();
    }

    @AfterAll
    static void afterAll() {
        redis.stop();
    }

    @Test
    public void shouldReturnValidInsuranceDataInFullyIsolatedEnvironment(CapturedOutput output) {
        // GIVEN: redis is available for Togglz
        assertTrue(redis.isRunning());

        // AND: url for calling the test data from MockInsuranceRepository provided
        var personaId = MockInsuranceRepository.TEST_ID;
        var url = String.format("http://localhost:%d%s/person/%s", port, InsuranceController.ENTRY, personaId);
        logger.info(() -> format("url: {0}", url));

        // WHEN: execute
        var restTemplate = new TestRestTemplate();
        var rawResponse = restTemplate.getForEntity(url, String.class);

        // THEN: expected JSON in response
        assertEquals(200, rawResponse.getStatusCode().value());
        assertNotNull(rawResponse.getBody());

        logger.info(() -> format("response body: {0}", rawResponse.getBody()));

        // AND: in Logs should be visible that we run a fallback to Vehicle.EMPTY
        assertThat(output).contains("Failed to fetch vehicle information. Fallback to EMPTY.");
    }

    @Test
    public void shouldSkipVehicleCallDueToTogglzDisabledFlag(CapturedOutput output) {
        // GIVEN: redis is available for Togglz
        assertTrue(redis.isRunning());

        // AND: feature toggle set to FALSE for USE_VEHICLE_SERVICE
        featureManager.setFeatureState(new FeatureState(Features.USE_VEHICLE_SERVICE).disable());
        assertFalse(featureManager.isActive(Features.USE_VEHICLE_SERVICE), "Feature toggle should be disabled");

        var personaId = MockInsuranceRepository.TEST_ID;
        var url = String.format("http://localhost:%d%s/person/%s", port, InsuranceController.ENTRY, personaId);
        logger.info(() -> format("url: {0}", url));

        // WHEN: execute (demo the get List<Insurance> approach instead of array)
        var restTemplate = InsuranceDeserializer.createRestTemplateForInsurance();
        var response = InsuranceDeserializer.getInsuranceList(restTemplate, url);

        // THEN: expected Several insurances in response
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        
        // AND: verify logs contain message about skipping vehicle service call
        assertThat(output).contains("Vehicle service call disabled by feature toggle");
    }
}
