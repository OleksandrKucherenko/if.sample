package com.sample.insurance.service;

import com.sample.insurance.model.Insurance;
import java.util.List;

/**
 * Service interface for insurance operations.
 */
public interface InsuranceService {
    
    /**
     * Retrieves all insurances for a person identified by their personal ID number.
     *
     * @param personalIdNumber The personal identification number of the person
     * @return List of insurances for the person
     */
    List<Insurance> getInsurancesByPersonalId(String personalIdNumber);
}
