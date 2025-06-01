# IF Sample

## Architecture Decision Records

```text
.
├── doc
│   └── adr
│       └── 0001-project-initialization.md
│       └── ...
└── README.md
```

* [1. Project Initialization](./doc/adr/0001-project-initialization.md)
* [2. Configure Java Version](./doc/adr/0002-configure-java-version.md)
* [3. Setup SpringBoot project structure](./doc/adr/0003-setup-springboot-project-structure.md)
* [4. Healthcheck for Services](./doc/adr/0004-healthcheck-for-services.md)
* [5. Swagger or OpenAPI rest api definition](./doc/adr/0005-swagger-or-openapi-rest-api-definition.md)
* [6. Dockerize the Vehicle Service](./doc/adr/0006-dockerize-the-vehicle-service.md)
* [7. Implement Insurance Service](./doc/adr/0007-implement-insurance-service.md)
* [8. Structured Logs](./doc/adr/0008-structured-logs.md)
* [9. Distributed Tracing](./doc/adr/0009-distributed-tracing.md)
* [10. Feature Toggle Integration](./doc/adr/0010-feature-toggle-integration.md)
* [11. Green And Blue Deployment Strategy](./doc/adr/0011-green-and-blue-deployment-strategy.md)

## Initial Setup

```bash
# install DIRENV (https://direnv.net/) and setup shell hook (https://direnv.net/docs/hook.html)
brew install direnv
# OR: curl -sfL https://direnv.net/install.sh | bash

# allow the project to be loaded
direnv allow
```

After that start reading ADRs one-by-one to onboard and understand the project.

## Achievements

- [x] Architecture and Implementation
  - [x] simple project structure that allows to develop multiple microservices. Monorepo approach.
  - [x] Java 24, SpringBoot 3.5, Gradle KTS (Kotlin DSL)
  - [x] transparent dependencies management via TOML (`gradle/libs.versions.toml`)
  - [x] self-setup project, via DIRENV and BASH (Only DIRENV required for start working on project, everything else will be printed on terminal automatically)
    - [x] version control on project tools (e.g. `git`, `docker`, `java`)
  - [x] Dockerize the micro-services
  - [x] Activated Native Image optimization for micro-services (improve startup performance of the apps)
  - [x] Implemented structured logging for unified logs across micro-services (used Logdy, with potential to migrate to ELK stack or alternative)
  - [x] Implemented distributed tracing for unified traces across micro-services (used Jaeger). Observability.
  - [x] Implemented feature toggles (used Togglz)
  - [x] Implemented health checks for micro-services (with potential to apply metrics)
  - [x] Implemented Swagger for unified API documentation across micro-services
  - [x] Implemented Docker Compose for unified setup of micro-services (local dev environment)
  - [x] Non-bloacking Http clients (used Spring WebFlux)
  - [x] Basic Auth security (used Spring Security)
  - [x] Auto-generate service client from Swagger Definition (used OpenAPI Generator), Insurance --> Vehicle
    - [ ] Swagger definitions can be use for simple contract tests (compare the snapshots of the API definitions)
    - [ ] Schema Registry (AVRO, protobuffer, etc.)
    - [ ] API versioning (or in api path, or by custom header attribute)
  - [ ] Errors handling:
    - [x] Fallback to EMPTY data (prevent NULLs)
    - [x] Deafult SpringBoot error handling (e.g. 404)
    - [ ] Exceptions raising, Custom error codes
  - [ ] Fault tolerance:
    - [ ] Circuit Breaker (Prevent cascading failures by short-circuiting calls to services that are failing or slow. Use libraries like Resilience4j)
    - [ ] Bulkhead pattern (Isolate resources (threads, connections, memory) for different components or tenants to prevent one failing or slow service from consuming all resources. Achieve via separate thread pools or service instances.)
    - [ ] Backpressure / Rate limiter (Limit the rate of requests to a service to prevent it from being overwhelmed. Use libraries like Resilience4j)
    - [ ] Retry, Timeout (Set reasonable timeouts on service calls and retries with exponential backoff to avoid hanging and loading downstream services.)
    - [ ] Saga pattern (https://microservices.io/patterns/data/saga.html)
    - [ ] Event Driven Architecture (https://microservices.io/patterns/data/event-driven-architecture.html)
    - [ ] CQRS (Command Query Responsibility Segregation), etc.
    - [ ] Quota (limit the number of requests per tenant, Requests per minute, etc.)
  - [ ] Best practicies (performance, security, etc.)
    - [ ] REST API special design for heavy operations, batches, jobs
    - [ ] Streams support, deliver data in stream continuously to the client
    
- [x] Testing Approaches
  - [ ] Implemented several types of tests
    - [x] Test profile (springboot test profile with verbose logs)
    - [x] Unit tests (JUnit 5, Mockito)
    - [x] Integration tests (Spring Boot Test, RestTemplate)
    - [ ] End-to-end tests (testcontainers)
    - [ ] Performance/Load tests (Stubs, mockserver, OHA, WRK, K6, cadvisor)
    - [ ] Contract tests (Pact, WireMock)

- [ ] Continues Delivery
  - [ ] Implemented CI/CD pipeline (Github actions)
    - [x] Based on running Gradle build tasks of different types (e.g. `build`, `test`, `integrationTest`, `dockerBuild`, `dockerPush`). Build fails if one of the tasks fails.
    - [ ] Implemented Green/Blue deployment concept (used Github actions to demo the approach)
    - [ ] Deployment versioning strategy (binaries versioning, docker tags, git tags, semver)
  - [ ] Blue-Green deployment strategy description
    - [ ] Rollback
    - [ ] Scheduled delivery
    - [ ] "Shadow modes" (Deploy new version alongside old, compare results of old and new implementation, switch to new version if results are better)
    - [ ] Environments support (e.g. local dev, staging, production, performance, etc.)

- [ ] Documentation and Code Quality
  - [x] Used ADRs to document decisions on project (perfect for AI and human to understand project structure and decisions)
    - [x] Details are part of each ADR
  - [ ] Quality Assurance
    - [ ] Code coverage (JaCoCo)
    - [ ] Static code analysis (Checkstyle, PMD)
    - [ ] Code quality (SonarQube)
    - [ ] Code style/formatting (Spotless)
    - [ ] Git leaks (Gitleaks)

## Personal Reflection

1. To accomplish all solution tasks will be required ~30-40 hours

| Phase                      | Estimated Time |
| -------------------------- | -------------- |
| Understanding & Planning   | 2-3 hours      |
| Architecture & Design      | 3-4 hours      |
| Setup & Initial Dev        | 6-8 hours      |
| Feature Toggling Setup     | 3-4 hours      |
| Testing Implementation     | 5-6 hours      |
| CI/CD Pipeline Setup       | 3-4 hours      |
| Documentation & Reflection | 2-3 hours      |
| Bonus & Cleanup            | 2-4 hours      |

2. more and  more features enabled makes a project dependencies more complex and bloated. 

As example: multiple redis client dependencies involed into project, but do we actually need this? 
By unified Redis client we can reduce the number of the dependencies and make project attack landscape smaller.

3. Not all solutions start to work immediately. Latest versions of each tool usually require some additional steps to make it work.

Example: Jaeger does not want to work gRPC out of the box. I have to switch solution to HTTP/protobuf port.

4. Cross Platform development will require additional steps to make it work well. 