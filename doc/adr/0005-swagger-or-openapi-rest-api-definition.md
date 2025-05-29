# 5. Swagger or OpenAPI rest api definition

Date: 2025-05-29

## Status

Accepted

## Context

To make communication between services "stable" and "predictable" we need to define rest api contract.

## Decision

- Use OpenAPI 3.0 to define rest api contract.
- Autogenerate OpenAPI contract using Springdoc.
- Use Swagger UI to visualize OpenAPI contract.
- Use swagger annotations to define rest api contract details, descriptions, examples, etc.

```bash
# run service first
./gradlew services:vehicle:bootRun

# to open Swagger UI
open http://localhost:50080/swagger-ui/index.html

# to get OpenAPI contract in JSON
http http://localhost:50080/v3/api-docs
```

## Consequences

- swagger definition will be used by insurance service to call vehicle service.
- depenedencies: 
  - `io.swagger.v3.oas.annotations` - from swagger-core

## References

- https://swagger.io/
- https://github.com/swagger-api/swagger-core
- https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#schemaObject
- https://docs.swagger.io/swagger-core/v2.0.0-RC3/apidocs/allclasses-noframe.html


