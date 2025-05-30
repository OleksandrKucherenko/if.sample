package com.sample.insurance.repository;

import com.sample.insurance.model.Insurance;
import reactor.core.publisher.Mono;

import java.util.List;

public interface InsuranceRepository {
    Mono<List<Insurance>> findByPersonalId(String personalIdNumber);
}
