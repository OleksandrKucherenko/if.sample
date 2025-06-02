# 13. Test Containers Setup

Date: 2025-06-02

## Status

Accepted

## Context

Demonstrate e2e testing using TestContainers.

## Decision

- Use Redis E2E dependency to demonstrate the TestContainers setup

- Optional Dependencies:
  - `io.rest-assured:rest-assured` (latest version: 5.5.5)

## Consequences

- To make tests running well we should implement JSON deserialization of the Insurance class
- Enabled Togglz unit tests integration 

```bash
# run tests with verbose logging
./gradlew :services:insurance:test --info

# force test re-run
./gradlew :services:insurance:test --info --rerun-tasks
```

- added into docker compose web ui to redis, for easier debugging

```bash
# to run only redisinsight web ui
docker compose --profile ui up -d redisinsight

open http://localhost:8001 # access web ui
# use connection string `redis:6379` with default credentials.
```

## References

- https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/
- https://mvnrepository.com/artifact/io.rest-assured/rest-assured, https://rest-assured.io/
- https://spring.io/blog/2023/06/23/improved-testcontainers-support-in-spring-boot-3-1
- https://medium.com/@disciullovincenzo/pills-creating-unit-tests-for-logs-in-java-using-spring-boot-and-capturedoutput-af54d663544e - Capture logs during unit tests

---

[Prev](./0012-establish-quality-standards.md) | [Next](./0014-github-action-ci-and-cd.md)