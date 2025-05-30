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
- dependencies:
	- `org.springframework.boot:spring-boot-starter-actuator` - Prerequisite for observability
  - `io.micrometer:micrometer-registry-otlp` - For Metrics
  - `io.micrometer:micrometer-tracing-bridge-brave` - For Tracing
  - `io.zipkin.contrib.otel:encoder-brave` - For Latency Visualization
  - `com.github.loki4j:loki-logback-appender` - For pushing logs out 


## References

- https://github.com/jaegertracing/jaeger/tree/v2.6.0/docker-compose/monitor
- https://spring.io/blog/2024/10/28/lets-use-opentelemetry-with-spring
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/
- 