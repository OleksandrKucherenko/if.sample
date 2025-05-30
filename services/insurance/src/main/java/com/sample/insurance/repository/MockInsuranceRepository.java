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
     * Test users for different test scenarios
     */
    public static final String TEST_ID = "920328-4428"; // Original test user - Maria Haglund with mixed insurances
    public static final String MULTI_CAR_USER = "680211-9047"; // User with multiple car insurances
    public static final String NO_INSURANCE_USER = "970811-5515"; // User with no insurances (edge case)
    public static final String HEALTH_ONLY_USER = "960509-5158"; // User with only health insurance
    public static final String MULTI_PET_USER = "860928-6607"; // User with multiple pet insurances
    public static final String MANY_INSURANCE_USER = "940316-6045"; // User with many insurances of all types

    /**
     * Mocked data with various test scenarios.
     */
    private final Map<String, List<Insurance>> mockedData;
    
    public MockInsuranceRepository() {
        // Build a more complex mock data map with all the test scenarios
        Map<String, List<Insurance>> data = new HashMap<>();
        
        // Original test user - Maria Haglund with mixed insurances
        data.put(TEST_ID, List.of(
                PetInsurance.from(TEST_ID, "Dog", "Rex"),
                HealthInsurance.from(TEST_ID, "Premium"),
                CarInsurance.from(TEST_ID, "ABC123")
        ));
        
        // User with multiple car insurances (edge case: multiple vehicles)
        data.put(MULTI_CAR_USER, List.of(
                CarInsurance.from(MULTI_CAR_USER, "XYZ789"),
                CarInsurance.from(MULTI_CAR_USER, "DEF456"),
                CarInsurance.from(MULTI_CAR_USER, "GHI123")
        ));
        
        // User with no insurances (edge case: empty list)
        data.put(NO_INSURANCE_USER, Collections.emptyList());
        
        // User with only health insurance
        data.put(HEALTH_ONLY_USER, List.of(
                HealthInsurance.from(HEALTH_ONLY_USER, "Basic")
        ));
        
        // User with multiple pet insurances (edge case: multiple pets)
        data.put(MULTI_PET_USER, List.of(
                PetInsurance.from(MULTI_PET_USER, "Cat", "Whiskers"),
                PetInsurance.from(MULTI_PET_USER, "Dog", "Buddy"),
                PetInsurance.from(MULTI_PET_USER, "Bird", "Tweety")
        ));
        
        // User with many insurances of all types (edge case: large number of policies)
        data.put(MANY_INSURANCE_USER, List.of(
                CarInsurance.from(MANY_INSURANCE_USER, "AAA111"),
                CarInsurance.from(MANY_INSURANCE_USER, "BBB222"),
                PetInsurance.from(MANY_INSURANCE_USER, "Dog", "Max"),
                PetInsurance.from(MANY_INSURANCE_USER, "Cat", "Luna"),
                HealthInsurance.from(MANY_INSURANCE_USER, "Premium"),
                HealthInsurance.from(MANY_INSURANCE_USER, "Dental")
        ));
        
        this.mockedData = Collections.unmodifiableMap(data);
    }

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
