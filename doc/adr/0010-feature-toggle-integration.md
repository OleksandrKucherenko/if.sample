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
  - a/b testing - demonstrate as switching between MockInsuranceRepository and RedisInsuranceRepository for 50% of the request (request DB for insurance data); (USE_REDIS_INSURANCE_REPOSITORY, custom LimitedABTestingStrategy with 50% of the request and 100 max activations)
  - green/blue deployment - will be used by CI/CD, we will access feature flag by REST API; (GREEN_BLUE_DEPLOYMENT, GradualActivationStrategy with 5% of the request)

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
    TogglzConfig -->|bean| Features -.->|implements| Feature([Feature])

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
    FTVSC -.->|instance| VSC(VehicleServiceClientImpl) -->|implements| VSC1([VehicleServiceClient])
    FTVSC -.->|instance| FTS
  ```

## Testing

### Admin console

```bash
# login: admin
# password: admin
open http://localhost:50081/togglz-console
```

Security configuration done via `SecurityConfig` class and `TogglzConfig` class.
TogglzConfig.getUserProvider() returns `SpringSecurityUserProvider` with role `ROLE_ADMIN`. This role created by `SecurityConfig` class in `userDetailsService()` method. 

> Note: prefix `ROLE_` is required for role names in Spring Security. So `ROLE_ADMIN` and `ADMIN` represents the same role but in different formats/contexts.

### Actuator togglz endpoint

```bash
http http://localhost:50081/actuator/togglz
```

Be sure that actuator publishing is activated in `application.properties`: 

```properties
management.endpoints.web.exposure.include=health,info,togglz
```

```json
[
    {
        "enabled": false,
        "metadata": {
            "attributes": {},
            "enabledByDefault": false,
            "groups": [],
            "label": "Green/Blue Deployment"
        },
        "name": "GREEN_BLUE_DEPLOYMENT",
        "params": {},
        "strategy": null
    },
    {
        "enabled": false,
        "metadata": {
            "attributes": {},
            "enabledByDefault": false,
            "groups": [],
            "label": "Use Redis Insurance Repository"
        },
        "name": "USE_REDIS_INSURANCE_REPOSITORY",
        "params": {},
        "strategy": null
    },
    {
        "enabled": true,
        "metadata": {
            "attributes": {},
            "enabledByDefault": true,
            "groups": [],
            "label": "Use Vehicle Service"
        },
        "name": "USE_VEHICLE_SERVICE",
        "params": {},
        "strategy": null
    }
]
```

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
- https://github.com/heneke/thymeleaf-extras-togglz - can be used for A/B testing on web ui, Thymeleaf template engine integration.
- https://www.togglz.org/documentation/activation-strategies - activation strategies for implementing A/B testing with limited test size.
- https://upstash.com/ - Redis as a Free Tier (100 MB storage, 10k req/day), perfect for CI/CD management on GitHub Actions (alternatives: https://redis.com/redis-enterprise/cloud/,  https://fly.io/, Render.com - postgresql for free)

---

[Prev](./0009-distributed-tracing.md) | [Next](./0011-green-and-blue-deployment-strategy.md)