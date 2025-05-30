package com.sample.insurance.service;

import com.sample.insurance.model.CarInsurance;
import com.sample.insurance.model.Insurance;
import com.sample.insurance.model.Vehicle;
import com.sample.insurance.repository.MockInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of InsuranceService that uses a mock repository
 * and integrates with Vehicle Service for car insurance details.
 */
@Service
public class InsuranceServiceImpl implements InsuranceService {
    
    private final MockInsuranceRepository insuranceRepository;
    private final VehicleServiceClient vehicleServiceClient;
    
    @Autowired
    public InsuranceServiceImpl(
            MockInsuranceRepository insuranceRepository,
            VehicleServiceClient vehicleServiceClient) {
        this.insuranceRepository = insuranceRepository;
        this.vehicleServiceClient = vehicleServiceClient;
    }
    
    /**
     * {@inheritDoc}
     * Also enriches car insurances with vehicle information from Vehicle Service.
     */
    @Override
    public List<Insurance> getInsurancesByPersonalId(String personalIdNumber) {
        List<Insurance> insurances = insuranceRepository.findByPersonalId(personalIdNumber);
        
        // Enrich car insurances with vehicle information
        insurances.stream()
                .filter(insurance -> insurance instanceof CarInsurance)
                .map(insurance -> (CarInsurance) insurance)
                .forEach(carInsurance -> {
                    try {
                        Vehicle vehicle = vehicleServiceClient.getVehicleByRegistrationNumber(
                                carInsurance.getRegistrationNumber());
                        carInsurance.setVehicle(vehicle);
                    } catch (Exception e) {
                        // Log the error but continue processing other insurances
                        System.err.println("Failed to fetch vehicle information: " + e.getMessage());
                    }
                });
        
        return insurances;
    }
}
