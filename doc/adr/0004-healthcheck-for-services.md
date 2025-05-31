# 4. Healthcheck for Services

Date: 2025-05-29

## Status

Accepted

## Context

Rest API service will need a healthcheck endpoint to monitor the service status. 
Will be used for cloud deployments and docker healthcheck.

## Decision

Use Spring Boot Actuator to expose healthcheck endpoint.

## Consequences

```bash
# run service
./gradlew services:vehicle:bootRun

# verify healthcheck with help of HTTPie
http http://localhost:50080/actuator/health
```

Response structure: 

```json
{
    "components": {
        "diskSpace": {
            "details": {
                "exists": true,
                "free": 1470502350848,
                "path": "/Users/oleksandr.kucherenko/workspace/github/if.sample/services/vehicle/.",
                "threshold": 10485760,
                "total": 1995218165760
            },
            "status": "UP"
        },
        "ping": {
            "status": "UP"
        },
        "ssl": {
            "details": {
                "invalidChains": [],
                "validChains": []
            },
            "status": "UP"
        }
    },
    "status": "UP"
}
```

## References

- https://www.baeldung.com/spring-boot-health-indicators


---

[Prev](./0003-setup-springboot-project-structure.md) | [Next](./0005-swagger-or-openapi-rest-api-definition.md)