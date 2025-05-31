package com.sample.insurance.repository;

import com.sample.insurance.model.Insurance;
import com.sample.insurance.service.FeatureToggleService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Repository selector that switches between MockInsuranceRepository and RedisInsuranceRepository
 * based on the feature toggle (USE_REDIS_INSURANCE_REPOSITORY).
 * This is used for A/B testing between the two repository implementations.
 */
@Component
public class InsuranceRepositorySelector implements InsuranceRepository {

    private final InsuranceRepository mockRepository;
    private final InsuranceRepository redisRepository;
    private final FeatureToggleService featureToggleService;

    public InsuranceRepositorySelector(
            MockInsuranceRepository mockRepository,
            RedisInsuranceRepository redisRepository,
            FeatureToggleService featureToggleService) {
        this.mockRepository = mockRepository;
        this.redisRepository = redisRepository;
        this.featureToggleService = featureToggleService;
    }

    /**
     * Selects the appropriate repository based on the feature toggle.
     *
     * @param personalIdNumber Personal identification number
     * @return List of insurances for the person
     */
    @Override
    public Mono<List<Insurance>> findByPersonalId(String personalIdNumber) {
        InsuranceRepository instance = mockRepository;

        if (featureToggleService.shouldUseRedisInsuranceRepository()) {
            instance = redisRepository;
        }

        return instance.findByPersonalId(personalIdNumber);
    }
}
