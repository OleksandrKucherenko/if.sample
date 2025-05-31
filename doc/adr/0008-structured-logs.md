# 8. Structured Logs

Date: 2025-05-30

## Status

Accepted

## Context

For tracking execution of the multiple services we need unified logging format. 

## Decision

- Use Logback with JSON layout for structured logging.
- Each request should have own unique traceId.
- Prepare services for open telemetry use.
- We should enhance your logs with trace context fields for correlation:
	- Typical fields: traceId, spanId, and possibly parentId.

Common logs pattern:
```xml
<pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg [traceId=%X{traceId}, spanId=%X{spanId}]%n</pattern>
```

## Consequences

- for tracing requests we want to use [Jagger](https://www.jaegertracing.io/) solution.
- make jagger a part of the docker-compose environment, but run in separated profile - "UI".
- dependencies: `io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter`

```bash
# run jagger web UI
open http://localhost:16686

# trigger API call
http http://localhost:50081/insurances/person/920328-4428
```

## References

- https://github.com/jaegertracing/jaeger/tree/v2.6.0/docker-compose/monitor
- https://spring.io/blog/2024/10/28/lets-use-opentelemetry-with-spring
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/getting-started/ 
- https://github.com/open-telemetry/opentelemetry-java-examples/tree/main/spring-native
- Vendor-neutral application observability facade
  https://micrometer.io/, Micrometer provides a facade for the most popular observability systems, allowing you to instrument your JVM-based application code without vendor lock-in. Think SLF4J, but for observability.