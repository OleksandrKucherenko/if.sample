package com.sample.insurance.repository;

import com.sample.insurance.model.Insurance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Redis implementation of InsuranceRepository.
 * This implementation is used for A/B testing with the MockInsuranceRepository.
 * In a real-world scenario, this would contain actual Redis operations.
 */
@Repository
public class RedisInsuranceRepository implements InsuranceRepository {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReactiveRedisTemplate<String, Insurance> redisTemplate;
    private final MockInsuranceRepository mockRepository;

    public RedisInsuranceRepository(ReactiveRedisTemplate<String, Insurance> redisTemplate, 
                                   MockInsuranceRepository mockRepository) {
        this.redisTemplate = redisTemplate;
        this.mockRepository = mockRepository;
    }

    /**
     * Find insurances by personal ID.
     * For demonstration purposes, this method delegates to the mock repository
     * but in a real application, it would fetch data from Redis.
     *
     * @param personalIdNumber Personal identification number
     * @return List of insurances for the person
     */
    @Override
    public Mono<List<Insurance>> findByPersonalId(String personalIdNumber) {
        // In a real implementation, we would fetch from Redis:
        // return redisTemplate.opsForList()
        //         .range("insurance:" + personalIdNumber, 0, -1)
        //         .collectList();

        // For this demo, we'll still use the mock data but log the Redis repository usage
        logger.info("redis template: {} personaId: {}", redisTemplate != null, personalIdNumber);

        // TODO (olku): implement caching via redis
        return mockRepository.findByPersonalId(personalIdNumber);
    }
}
