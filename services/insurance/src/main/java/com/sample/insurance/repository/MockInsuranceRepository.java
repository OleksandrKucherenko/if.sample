package com.sample.insurance.repository;

import com.sample.insurance.model.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mock repository for insurance data.
 * This is a temporary implementation until we integrate with a real database.
 */
@Repository
public class MockInsuranceRepository {
    
    private final Map<String, List<Insurance>> insurancesByPersonalId = new HashMap<>();
    
    public MockInsuranceRepository() {
        initializeTestData();
    }
    
    /**
     * Initialize test data for the mock repository.
     * Contains insurance data for the test user with personal ID: 920328-4428
     */
    private void initializeTestData() {
        // Test user from ADR: Maria Haglund, personalIdNumber: 920328-4428
        String testUserPersonalId = "920328-4428";
        List<Insurance> insurances = new ArrayList<>();
        
        // Add a pet insurance
        insurances.add(new PetInsurance(
                UUID.randomUUID(),
                testUserPersonalId,
                "Dog",
                "Rex"
        ));
        
        // Add a health insurance
        insurances.add(new HealthInsurance(
                UUID.randomUUID(),
                testUserPersonalId,
                "Premium"
        ));
        
        // Add a car insurance
        insurances.add(new CarInsurance(
                UUID.randomUUID(),
                testUserPersonalId,
                "ABC123"
        ));
        
        // Store the insurances for the test user
        insurancesByPersonalId.put(testUserPersonalId, insurances);
    }
    
    /**
     * Get all insurances for a person by their personal ID.
     *
     * @param personalIdNumber Personal identification number
     * @return List of insurances for the person or empty list if none found
     */
    public List<Insurance> findByPersonalId(String personalIdNumber) {
        return insurancesByPersonalId.getOrDefault(personalIdNumber, Collections.emptyList());
    }
}
