# Distributed Tracing with Jaeger

This document explains how to use the distributed tracing functionality that has been implemented as per the [ADR-0009](./adr/0009-distributed-tracing.md).

## Overview

Distributed tracing has been implemented using:
- [OpenTelemetry](https://opentelemetry.io/) for instrumentation
- [Jaeger](https://www.jaegertracing.io/) for tracing visualization

## Running with Distributed Tracing

The tracing infrastructure is part of a separate profile called "ui" in the docker-compose configuration. This allows you to run the services without the overhead of tracing when not needed.

### Starting Services with Jaeger

To start all services including Jaeger:

```bash
# Start all services including those in the UI profile
docker-compose --profile ui up -d
```

To start only the core services without Jaeger:

```bash
# Start only the core services
docker-compose up -d
```

### Accessing the Jaeger UI

Once the services are running with the UI profile, you can access the Jaeger UI at:

```bash
# Open Jaeger UI in your browser
open http://localhost:16686
```

### Generating Traces

To generate traces, simply make requests to the services. For example:

```bash
# Request insurance information for a person
http http://localhost:50081/insurances/person/920328-4428
```

Each request will generate a trace with a unique traceId that will be propagated across service boundaries.

## Configuration Details

The distributed tracing implementation includes:

1. **Dependencies**: 
   - `io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter`
   - `io.micrometer:micrometer-tracing-bridge-otel`

2. **Environment Configuration**:
   Each service is configured with:
   - `OTEL_EXPORTER_OTLP_ENDPOINT` - Points to the Jaeger collector
   - `OTEL_SERVICE_NAME` - Identifies the service in traces

3. **Logging Integration**:
   - Trace IDs and Span IDs are included in log patterns
   - Format: `%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]`

## Viewing and Analyzing Traces

In the Jaeger UI:

1. Select a service from the dropdown
2. Set the desired time range
3. Click "Find Traces" to view all traces for that service
4. Click on a specific trace to see its details, including:
   - The full trace timeline
   - Individual spans and their durations
   - Tags and logs associated with each span
   - Service dependencies

## Troubleshooting

If traces are not appearing in Jaeger UI:

1. Ensure the services are running with the UI profile: `docker-compose --profile ui ps`
2. Verify the Jaeger service is healthy: `docker logs jaeger`
3. Check that the OTEL environment variables are correctly set in the containers
4. Ensure that HTTP requests are being made between services
