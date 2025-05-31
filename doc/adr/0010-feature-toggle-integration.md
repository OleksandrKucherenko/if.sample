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
  - a/b testing - demonstrate as switching between MockInsuranceRepository and RedisInsuranceRepository for 50% of the request (request DB for insurance data); (USE_DB_INSURANCE_REPOSITORY)
  - green/blue deployment - will be used by CI/CD, we will access feature flag by REST API; (GREEN_BLUE_DEPLOYMENT)

## Consequences

- minimalistic basic auth required for feature toggle web ui access
  - hardcoded user credentials: admin/admin (username/password)
- we will use version 4.4.0 (https://github.com/togglz/togglz/releases)
- dependencies: 
  - `org.togglz:togglz-spring-boot-starter` (keep it in TOML as `spring-boot-starter-togglz`), ref: https://mvnrepository.com/artifact/org.togglz/togglz-spring-boot-starter, version 4.4.0
  - 

## References

- https://www.togglz.org/documentation/admin-console - web ui
- https://www.baeldung.com/spring-togglz - Setup + Unit Tests
- https://medium.com/tuanhdotnet/methods-for-implementing-feature-flag-management-in-your-spring-boot-application-02d38811a58b
