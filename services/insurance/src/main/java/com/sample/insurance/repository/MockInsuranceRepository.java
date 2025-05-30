package com.sample.insurance.repository;

import com.sample.insurance.model.*;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Mock repository for insurance data.
 * This is a temporary implementation until we integrate with a real database.
 */
@Repository
public class MockInsuranceRepository implements InsuranceRepository {
    /**
     * Test user from ADR: Maria Haglund, personalIdNumber: 920328-4428
     */
    public static final String TEST_ID = "920328-4428";
    /**
     * Mocked data.
     */
    private final Map<String, List<Insurance>> mockedData = Map.of(
            TEST_ID,
            List.of(
                    PetInsurance.from(TEST_ID, "Dog", "Rex"),
                    HealthInsurance.from(TEST_ID, "Premium"),
                    CarInsurance.from(TEST_ID, "ABC123")
            )
    );

    /**
     * Get all insurances for a person by their personal ID.
     *
     * @param personalIdNumber Personal identification number
     * @return List of insurances for the person or empty list if none found
     */
    @Override
    public Mono<List<Insurance>> findByPersonalId(String personalIdNumber) {
        return Mono.just(mockedData.getOrDefault(personalIdNumber, Collections.emptyList()));
    }
}
