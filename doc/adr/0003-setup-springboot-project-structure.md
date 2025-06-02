# 3. Setup SpringBoot project structure

Date: 2025-05-29

## Status

Accepted

## Context

- use https://start.spring.io - as a bootstrap for the project: Gradle Kotlin, Java, Spring Boot v3.5, Java v24 (Amazon Corretto)

## Decision

- Create Multi-module project structure
- Use TOML for dependencies management
- place each service in separate module (e.g. services/vehicle)

## Consequences

- `*Application` classes required for minimal setup
- How to Run? How to Test?

```bash
# list available tasks
./gradlew tasks
./gradlew tasks --all # All tasks for all included modules

# test run, local developer env
./gradlew services:vehicle:bootRun

# run unit tests in continues mode
./gradlew test -t --rerun-tasks
```

- Assign non-standard ports for avoiding conflicts with any other project (reserved range: 50080-50090)
- `default` profile reserved for local development.

Active profile (or multiple) can be configured in `application.properties` or by global env variable:

```bash
export SPRING_PROFILES_ACTIVE=default,dev,test
```

Each profile should have own configuration file: `application-${profileName}.properties`

- To run multiple services at the same time can be used `foreman` tool:

```bash
# install tool
brew install foreman

# create Procfile with content
vehicle:   ./gradlew :services:vehicle:bootRun
insurance: ./gradlew :services:insurance:bootRun

# run multiple process at once
foreman start
```

- create `test` profile with verbose logging - `application-test.properties`
- make `test` profile default for unit tests, `@ActiveProfiles("test")`
- enable virtual threads for lightweight servers (Tomcat, Jetty, Undertow)
- libraries versions are defined inside the `gradle/libs.versions.toml` file

```bash
# run service
./gradlew services:vehicle:bootRun

# validate endpoint
http http://localhost:50080/vehicles/nmg52y # expected OK 200

# default error handling
http http://localhost:50080/vehicles/nmg52y/test # expected NotFound 404
```

```jsonc
{
    // UUID is random on each call
    "id": "385977df-99df-4fa9-9f1f-1126365aa8f8",
    "registrationNumber": "nmg52y"
}
```

## References

- https://plugins.gradle.org/search?term=git+hooks
- https://docs.spring.io/spring-boot/gradle-plugin/
- https://ddollar.github.io/foreman/, https://formulae.brew.sh/formula/foreman#default
- https://www.baeldung.com/rest-template
- https://github.com/mockito/mockito, https://site.mockito.org/
- https://github.com/spring-io/initializr/issues/1590 - Remove Mockito warning from logs
- https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html - Mockito setup
- https://steelcityamir.com/blog/testing-log-statements-in-spring-boot/ - logs capturing during tests

---

[Prev](./0002-configure-java-version.md) | [Next](./0004-healthcheck-for-services.md)