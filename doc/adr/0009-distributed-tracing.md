# 9. Distributed Tracing

Date: 2025-05-31

## Status

Accepted

## Context

For microservices architecture we need to implement distributed tracing, espcially for cases when we
need to understand the sequence of calls between multiple services, latency and errors.

## Decision

- Use [Jagger](https://www.jaegertracing.io/) solution.
- Enable open telemetry in services.
- Make jagger a part of the docker-compose environment, but run in separated profile - "UI".

```bash
# run jagger web UI
open http://localhost:16686

# trigger API call
http http://localhost:50081/insurances/person/920328-4428
```

- Each request should have own unique traceId.
	- Typical fields: traceId, spanId, and possibly parentId.

- WebClients should always have registered ObservationRegistry.

## Consequences

- dependencies: 
  - `io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter` (but keep it named: `spring-boot-starter-opentelemetry` in TOML file)
  - `io.micrometer:micrometer-tracing-bridge-otel`

ref1: https://mvnrepository.com/artifact/io.opentelemetry.instrumentation/opentelemetry-spring-boot-starter, version 2.16.0
ref2: https://mvnrepository.com/artifact/io.micrometer/micrometer-tracing-bridge-otel, version 1.5.0
ref3: https://www.jaegertracing.io/docs/2.6/getting-started/, version 2.6.0 , docker image: `jaegertracing/jaeger:2.6.0`
  - https://hub.docker.com/r/jaegertracing/jaeger
  - https://hub.docker.com/r/jaegertracing/all-in-one - DEPRECATED!

```bash
# run jagger web UI
open http://localhost:16686

# trigger API call
http http://localhost:50081/insurances/person/920328-4428
```

- Metrics and Logs are not enabled for OpenTelemetry (to make it simple).

## Troubleshooting

```bash
# verify the content of the JAR binary
jar tf ./vehicle-0.0.1-SNAPSHOT.jar | grep application

# force Docker Compose to rebuild images before starting containers
docker compose up --build -d
```

## References

- https://github.com/jaegertracing/jaeger/tree/v2.6.0/docker-compose/monitor
- https://spring.io/blog/2024/10/28/lets-use-opentelemetry-with-spring
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/getting-started/ 
- https://github.com/open-telemetry/opentelemetry-java-examples/tree/main/spring-native
- Vendor-neutral application observability facade [Micrometer](https://micrometer.io/), Micrometer provides a facade for the most popular observability systems, allowing you to instrument your JVM-based application code without vendor lock-in. Think SLF4J, but for observability.

---

[Prev](./0008-structured-logs.md) | [Next](./0010-feature-toggle-integration.md)