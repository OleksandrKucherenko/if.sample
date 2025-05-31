# 8. Structured Logs

Date: 2025-05-30

## Status

Accepted

## Context

For tracking execution of the multiple services we need unified logging format. 

## Decision

- Use Logback with JSON layout for structured logging.
- We should enhance your logs with trace context fields for correlation:
    - Typical fields: traceId, spanId, and possibly parentId.
- Use Logdy for logs inspection.
- Enabled logs into JSONL format on disk. Rotated logs are stored in `logs` directory. 
  - Keep logs for 7 days.
  - Rotate logs every 5MB.
- Customize logs, include service name into each log line.

## How to Enable Structured Logs

1. **Add Dependency**
   
   In `gradle/libs.versions.toml`:
   ```toml
   [versions]
   # https://mvnrepository.com/artifact/net.logstash.logback/logstash-logback-encoder
   logstash-logback-encoder = "+" # replace with latest version
   
   [libraries]
   logstash-logback-encoder = { module = "net.logstash.logback:logstash-logback-encoder", version.ref = "logstash-logback-encoder" }
   ```
   
   In each service's `build.gradle.kts`:
   ```kotlin
   dependencies {
       implementation(libs.logstash.logback.encoder)
   }
   ```

2. **Add Logback Configuration**
   
   Create `src/main/resources/logback-spring.xml` in both `services/vehicle` and `services/insurance`:

   ```xml
  <configuration>
    <appender name="JSON_CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
      <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <customFields>{"service":"${spring.application.name:-insurance}"}</customFields>
      </encoder>
    </appender>
    <appender name="JSON_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <file>logs/app.jsonl</file>
      <rollingPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy">
        <fileNamePattern>logs/app.%d{yyyy-MM-dd}.%i.jsonl</fileNamePattern>
        <maxHistory>7</maxHistory>
        <maxFileSize>5MB</maxFileSize>
      </rollingPolicy>
      <encoder class="net.logstash.logback.encoder.LogstashEncoder">
        <customFields>{"service":"${spring.application.name:-insurance}"}</customFields>
      </encoder>
    </appender>
    <root level="INFO">
      <appender-ref ref="JSON_CONSOLE" />
      <appender-ref ref="JSON_FILE" />
    </root>
    <logger name="org.springframework.web" level="DEBUG"/>
  </configuration>
   ```

  Alternative:

  Elastic Common Schema (ECS), [Graylog Extended Log Format (GELF)](https://www.baeldung.com/graylog-with-spring-boot), and Logstash JSON.

   ```properties
   logging.structured.format.console=ecs
   ```

3. **Enable Request Logging**
   
   In `src/main/resources/application.properties`:

   ```properties
   logging.level.root=INFO
   logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG
   ```

4. **Verify Structured Logs**
   
   - Start both services:
     ```sh
     # build services
     ./gradlew :services:insurance:build :services:vehicle:build

     # run services
     foreman start
     ```
   - Inspect logs in the `logs` directory. Each log line should be a JSON object, including trace context fields if configured.
   - To view logs in real time with Logdy:
     ```sh
     # upload old logs and start following the changes
     cat logs/app.jsonl | logdy follow logs/app.jsonl --port 50090

     # open logdy in browser
     open http://localhost:50090

     # try search filter: `raw includes "GET"`
     # https://logdy.dev/docs/explanation/facets#semantic-filtering-via-searchbar
     ```
   - Or, if using Docker Compose:
     ```sh
     docker-compose logs -f | logdy --port 50090
     ```

## Consequences

- disable banner (not play well with structured logs), avoid noise

## References
- https://github.com/logstash/logstash-logback-encoder
- https://spring.io/guides/gs/spring-boot/
- https://github.com/jaegertracing/jaeger
- https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/
- https://logdy.dev/
- https://www.baeldung.com/spring-http-logging
- https://medium.com/@imvtsl/automate-spring-server-logging-6a344d6019c9