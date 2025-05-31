package com.sample.insurance.service;

import com.sample.insurance.model.CarInsurance;
import com.sample.insurance.model.Insurance;
import com.sample.insurance.model.Vehicle;
import com.sample.insurance.repository.InsuranceRepositorySelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of InsuranceService that uses a mock repository
 * and integrates with Vehicle Service for car insurance details.
 */
@Service
public class InsuranceServiceImpl implements InsuranceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final InsuranceRepositorySelector repository;
    private final FeatureToggleVehicleServiceClient vehicleServiceClient;

    @Autowired
    public InsuranceServiceImpl(
            InsuranceRepositorySelector insuranceRepositorySelector,
            FeatureToggleVehicleServiceClient featureToggleVehicleServiceClient) {
        this.repository = insuranceRepositorySelector;
        this.vehicleServiceClient = featureToggleVehicleServiceClient;
    }

    /**
     * {@inheritDoc}
     * Also enriches car insurances with vehicle information from Vehicle Service.
     */
    @Override
    public Mono<List<Insurance>> getInsurancesByPersonalId(final String personalIdNumber) {
        return repository.findByPersonalId(personalIdNumber)
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::fetchVehicleInformation)
                .collectList();
    }

    /** Extract vehicle information by extra call. */
    /* package */ Mono<Insurance> fetchVehicleInformation(final Insurance insurance) {
        // Enrich car insurances with vehicle information
        if (insurance instanceof CarInsurance ci) {
            return vehicleServiceClient.getVehicleByRegistrationNumber(ci.getRegistrationNumber())
                    .onErrorResume((error) -> {
                        logger.error("Failed to fetch vehicle information. Fallback to EMPTY.", error);
                        return Mono.just(Vehicle.EMPTY);
                    }).map(ci::setVehicle);
        }

        return Mono.just(insurance);
    }
}
