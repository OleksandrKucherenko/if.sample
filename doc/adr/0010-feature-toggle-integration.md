# 10. Feature Toggle Integration

Date: 2025-05-31

## Status

Accepted

## Context

We need to demonstrate the Feature Toggle integration, for use cases:
- enable/disable feature
- a/b testing
- green-blue deployment

## Decision

- We will use Togglz as a feature toggle solution
- Use Redis as a permanent storage for feature toggles, a/b testing and etc.
  - docker-compose should have a redis container setup for local runs
- Document ON/OFF feature toggle via `httpie` tool calls (command line operations)
- Enable Togglz web UI for remote feature toggle management
- Keep declaration of feature toggles in separate file, for easier updates
- We should demonstrate three use cases:
  - enable/disable feature - enable/disable call of vehicle service; (USE_VEHICLE_SERVICE)
  - a/b testing - demonstrate as switching between MockInsuranceRepository and RedisInsuranceRepository for 50% of the request (request DB for insurance data); (USE_REDIS_INSURANCE_REPOSITORY)
  - green/blue deployment - will be used by CI/CD, we will access feature flag by REST API; (GREEN_BLUE_DEPLOYMENT)

## Consequences

- minimalistic basic auth required for feature toggle web ui access
  - hardcoded user credentials: admin/admin (username/password)
- we will use version 4.4.0 (https://github.com/togglz/togglz/releases)
- dependencies: 
  - `org.togglz:togglz-spring-boot-starter` (keep it in TOML as `spring-boot-starter-togglz`), ref: https://mvnrepository.com/artifact/org.togglz/togglz-spring-boot-starter, version 4.4.0
  - `org.springframework.boot:spring-boot-starter-data-redis` enable Redis for springboot service

  Togglz Configuration:

  ```mermaid
  graph LR
    Features -.->|implements| Feature([Feature])

  ```

  Classes:

  ```mermaid
  graph TB
    IR([InsuranceRepository])
    MIR(MockInsuranceRepository) -->|implements| IR
    RIR(RedisInsuranceRepository) -->|implements| IR
    RIR -->|bean reactiveRedisInsuranceTemplate| RC(RedisConfig)
    RIR -.->|instance| MIR
    RC -.->|bean reactiveRedisConnectionFactory| ReactiveRedisConnectionFactory
    IRS(InsuranceRepositorySelector) -.->|instance| FTS(FeatureToggleService)
    IRS -.->|instance| MIR
    IRS -.->|instance| RIR
    ISI(InsuranceServiceImpl) -.->|instance| IRS
    ISI -.->|instance| FTVSC(FeatureToggleVehicleServiceClient)
    FTVSC -.->|instance| VSC(VehicleServiceClient) -->|implements| VSC1([VehicleServiceClient])
    FTVSC -.->|instance| FTS
  ```

## Testing

### Get API features

```bash
http http://localhost:50081/api/features
```

```json
{
    "GREEN_BLUE_DEPLOYMENT": false,
    "USE_REDIS_INSURANCE_REPOSITORY": false,
    "USE_VEHICLE_SERVICE": true
}
```

### Deployment Flag

```bash
http http://localhost:50081/api/features/deployment
```

```json
{
    "deploymentType": "GREEN",
    "enabled": false,
    "feature": "GREEN_BLUE_DEPLOYMENT",
    "timestamp": 1748701332663
}
```

### Runing services with JQ formatting for logs

Modify Procfile to run services with JQ formatting for logs. This will make logs more readable.

> Note: JSON logs are pushed to STDERR stream, so we need to redirect STDERR to STDOUT/STDIN

```text
vehicle:   java -jar services/vehicle/build/libs/vehicle-0.0.1-SNAPSHOT.jar 2>&1 | jq
insurance: java -jar services/insurance/build/libs/insurance-0.0.1-SNAPSHOT.jar 2>&1 | jq
```

## References

- https://www.togglz.org/documentation/admin-console - web ui
- https://www.baeldung.com/spring-togglz - Setup + Unit Tests
- https://medium.com/tuanhdotnet/methods-for-implementing-feature-flag-management-in-your-spring-boot-application-02d38811a58b
- https://hub.docker.com/_/redis 