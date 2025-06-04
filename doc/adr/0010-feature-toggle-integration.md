# 10. Feature Toggle Integration

Date: 2025-05-31

## Status

Accepted

## Context

We need to demonstrate the Feature Toggle integration, for use cases:
- enable/disable feature
- a/b testing
- green-blue deployment

## Decision

- We will use Togglz as a feature toggle solution
- Use Redis as a permanent storage for feature toggles, a/b testing and etc.
  - docker-compose should have a redis container setup for local runs
- Document ON/OFF feature toggle via `httpie` tool calls (command line operations)
- Enable Togglz web UI for remote feature toggle management
- Keep declaration of feature toggles in separate file, for easier updates
- We should demonstrate three use cases:
  - enable/disable feature - enable/disable call of vehicle service; (USE_VEHICLE_SERVICE)
  - a/b testing - demonstrate as switching between MockInsuranceRepository and RedisInsuranceRepository for 50% of the request (request DB for insurance data); (USE_REDIS_INSURANCE_REPOSITORY, custom LimitedABTestingStrategy with 50% of the request and 100 max activations)
  - green/blue deployment - will be used by CI/CD, we will access feature flag by REST API; (GREEN_BLUE_DEPLOYMENT, GradualActivationStrategy with 5% of the request)

## Consequences

- minimalistic basic auth required for feature toggle web ui access
  - hardcoded user credentials: admin/admin (username/password)
- we will use version 4.4.0 (https://github.com/togglz/togglz/releases)
- dependencies: 
  - `org.togglz:togglz-spring-boot-starter` (keep it in TOML as `spring-boot-starter-togglz`), ref: https://mvnrepository.com/artifact/org.togglz/togglz-spring-boot-starter, version 4.4.0
  - `org.springframework.boot:spring-boot-starter-data-redis` enable Redis for springboot service

  Togglz Configuration:

  ```mermaid
  graph LR
    TogglzConfig -->|bean| Features -.->|implements| Feature([Feature])

  ```

  Classes:

  ```mermaid
  graph TB
    IR([InsuranceRepository])
    MIR(MockInsuranceRepository) -->|implements| IR
    RIR(RedisInsuranceRepository) -->|implements| IR
    RIR -->|bean reactiveRedisInsuranceTemplate| RC(RedisConfig)
    RIR -.->|instance| MIR
    RC -.->|bean reactiveRedisConnectionFactory| ReactiveRedisConnectionFactory
    IRS(InsuranceRepositorySelector) -.->|instance| FTS(FeatureToggleService)
    IRS -.->|instance| MIR
    IRS -.->|instance| RIR
    ISI(InsuranceServiceImpl) -.->|instance| IRS
    ISI -.->|instance| FTVSC(FeatureToggleVehicleServiceClient)
    FTVSC -.->|instance| VSC(VehicleServiceClientImpl) -->|implements| VSC1([VehicleServiceClient])
    FTVSC -.->|instance| FTS
  ```

## Testing

### Admin console

```bash
# login: admin
# password: admin
open http://localhost:50081/togglz-console
```

Security configuration done via `SecurityConfig` class and `TogglzConfig` class.
TogglzConfig.getUserProvider() returns `SpringSecurityUserProvider` with role `ROLE_ADMIN`. This role created by `SecurityConfig` class in `userDetailsService()` method. 

> Note: prefix `ROLE_` is required for role names in Spring Security. So `ROLE_ADMIN` and `ADMIN` represents the same role but in different formats/contexts.

### Actuator togglz endpoint

```bash
http http://localhost:50081/actuator/togglz
```

Be sure that actuator publishing is activated in `application.properties`: 

```properties
management.endpoints.web.exposure.include=health,info,togglz
```

```json
[
    {
        "enabled": false,
        "metadata": {
            "attributes": {},
            "enabledByDefault": false,
            "groups": [],
            "label": "Green/Blue Deployment"
        },
        "name": "GREEN_BLUE_DEPLOYMENT",
        "params": {},
        "strategy": null
    },
    {
        "enabled": false,
        "metadata": {
            "attributes": {},
            "enabledByDefault": false,
            "groups": [],
            "label": "Use Redis Insurance Repository"
        },
        "name": "USE_REDIS_INSURANCE_REPOSITORY",
        "params": {},
        "strategy": null
    },
    {
        "enabled": true,
        "metadata": {
            "attributes": {},
            "enabledByDefault": true,
            "groups": [],
            "label": "Use Vehicle Service"
        },
        "name": "USE_VEHICLE_SERVICE",
        "params": {},
        "strategy": null
    }
]
```

### Get API features

```bash
http http://localhost:50081/api/features
```

```json
{
    "GREEN_BLUE_DEPLOYMENT": false,
    "USE_REDIS_INSURANCE_REPOSITORY": false,
    "USE_VEHICLE_SERVICE": true
}
```

### Deployment Flag

```bash
http http://localhost:50081/api/features/deployment
```

```json
{
    "deploymentType": "GREEN",
    "enabled": false,
    "feature": "GREEN_BLUE_DEPLOYMENT",
    "timestamp": 1748701332663
}
```

### Running services with JQ formatting for logs

Modify Procfile to run services with JQ formatting for logs. This will make logs more readable.

> Note: JSON logs are pushed to STDERR stream, so we need to redirect STDERR to STDOUT/STDIN

```text
vehicle:   java -jar services/vehicle/build/libs/vehicle-0.0.1-SNAPSHOT.jar 2>&1 | jq
insurance: java -jar services/insurance/build/libs/insurance-0.0.1-SNAPSHOT.jar 2>&1 | jq
```

## References

- https://www.togglz.org/documentation/admin-console - web ui
- https://www.baeldung.com/spring-togglz - Setup + Unit Tests
- https://medium.com/tuanhdotnet/methods-for-implementing-feature-flag-management-in-your-spring-boot-application-02d38811a58b
- https://hub.docker.com/_/redis 
- https://github.com/heneke/thymeleaf-extras-togglz - can be used for A/B testing on web ui, Thymeleaf template engine integration.
- https://www.togglz.org/documentation/activation-strategies - activation strategies for implementing A/B testing with limited test size.
- https://upstash.com/ - Redis as a Free Tier (100 MB storage, 10k req/day), perfect for CI/CD management on GitHub Actions (alternatives: https://redis.com/redis-enterprise/cloud/,  https://fly.io/, Render.com - postgresql for free)

## Implement REST API for Feature Togglz

Togglz does not provide a REST API out of the box for managing feature flags in a Spring Boot application. However, it offers a robust foundation that makes it straightforward to implement a custom REST API for feature flag management. Below, I’ll explain the details and provide guidance on how to approach this.

### Togglz Out-of-the-Box Support
Togglz provides the following built-in tools for managing feature flags, but none are REST APIs:
- **Togglz Admin Console**: A web-based UI (accessible by default at `http://localhost:8080/togglz-console`) for managing feature flags. This is included with the `togglz-console` dependency and allows manual toggling of features but is not an API.
- **Actuator Endpoint**: When using the `togglz-spring-boot-starter`, Togglz exposes an actuator endpoint (e.g., `http://localhost:8080/actuator/togglz`). This endpoint provides a JSON overview of all feature toggles and their states via a GET request and allows enabling/disabling features via POST requests. For example:
  - GET: `curl -GET http://localhost:8080/actuator/togglz`
    - Response: `[{"name":"FEATURE_ONE","enabled":true,"strategy":null,"params":{}}, ...]`
  - POST: `curl -d '{"name":"FEATURE_ONE","enabled":"true"}' -H "Content-Type: application/json" -POST http://localhost:8080/actuator/togglz/FEATURE_ONE`
  
  While this actuator endpoint functions as a REST-like interface, it is primarily designed for integration with Spring Boot’s actuator system and may not offer the full flexibility or customization some applications require (e.g., advanced filtering, user-specific toggles, or custom responses).

### Custom REST API Implementation
To create a fully customized REST API for managing Togglz feature flags, you need to implement it yourself. This is a common and straightforward process, as Togglz provides the `FeatureManager` class, which you can inject into your Spring Boot application to programmatically manage feature states. Below is an example of how to implement a custom REST API for Togglz.

#### Steps to Implement a Custom REST API
1. **Add Dependencies**
   Ensure you have the necessary Togglz dependencies in your `pom.xml` or `build.gradle`. For Maven:
   ```xml
   <dependency>
       <groupId>org.togglz</groupId>
       <artifactId>togglz-spring-boot-starter</artifactId>
       <version>3.1.2</version>
   </dependency>
   <dependency>
       <groupId>org.togglz</groupId>
       <artifactId>togglz-console</artifactId>
       <version>3.1.2</version>
   </dependency>
   ```
   For Gradle:
   ```gradle
   implementation 'org.togglz:togglz-spring-boot-starter:3.1.2'
   implementation 'org.togglz:togglz-console:3.1.2'
   ```

2. **Define Feature Flags**
   Create an enum implementing the `Feature` interface to define your feature flags:
   ```java
   import org.togglz.core.Feature;
   import org.togglz.core.annotation.Label;

   public enum MyFeatures implements Feature {
       @Label("Feature One")
       FEATURE_ONE,

       @Label("Feature Two")
       FEATURE_TWO;

       public boolean isActive() {
           return FeatureContext.getFeatureManager().isActive(this);
       }
   }
   ```

3. **Create a REST Controller**
   Implement a Spring Boot REST controller to manage feature flags using the `FeatureManager`. Below is an example:
   ```java
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.*;
   import org.togglz.core.manager.FeatureManager;
   import org.togglz.core.repository.FeatureState;
   import org.togglz.core.context.FeatureContext;

   @RestController
   @RequestMapping("/api/features")
   public class FeatureToggleController {

       private final FeatureManager featureManager;

       public FeatureToggleController(FeatureManager featureManager) {
           this.featureManager = featureManager;
       }

       // Get all feature states
       @GetMapping
       public ResponseEntity<?> getAllFeatures() {
           StringBuilder response = new StringBuilder();
           for (MyFeatures feature : MyFeatures.values()) {
               boolean isActive = featureManager.isActive(feature);
               response.append(feature.name()).append(" is ")
                       .append(isActive ? "enabled" : "disabled")
                       .append("\n");
           }
           return ResponseEntity.ok(response.toString());
       }

       // Get specific feature state
       @GetMapping("/{name}")
       public ResponseEntity<?> getFeatureState(@PathVariable String name) {
           try {
               MyFeatures feature = MyFeatures.valueOf(name);
               boolean isActive = featureManager.isActive(feature);
               return ResponseEntity.ok(new ToggleFeature(name, isActive));
           } catch (IllegalArgumentException e) {
               return ResponseEntity.badRequest().body("Feature " + name + " not found");
           }
       }

       // Enable or disable a feature
       @PostMapping("/{name}")
       public ResponseEntity<?> setFeatureState(@PathVariable String name, @RequestBody ToggleFeature resource) {
           try {
               MyFeatures feature = MyFeatures.valueOf(name);
               FeatureState featureState = new FeatureState(feature, resource.isActive());
               featureManager.setFeatureState(featureState);
               return ResponseEntity.ok(new ToggleFeature(name, featureManager.isActive(feature)));
           } catch (IllegalArgumentException e) {
               return ResponseEntity.badRequest().body("Feature " + name + " not found");
           }
       }
   }

   // DTO for feature state
   class ToggleFeature {
       private String name;
       private boolean active;

       public ToggleFeature() {}

       public ToggleFeature(String name, boolean active) {
           this.name = name;
           this.active = active;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public boolean isActive() {
           return active;
       }

       public void setActive(boolean active) {
           this.active = active;
       }
   }
   ```

4. **Configure Togglz**
   Ensure Togglz is configured in your Spring Boot application. The `togglz-spring-boot-starter` handles most of the auto-configuration, but you may want to customize the `StateRepository` (e.g., to use a persistent store like MongoDB or Redis) or `UserProvider` for advanced activation strategies. For example, to use a MongoDB repository:
   ```java
   import org.springframework.context.annotation.Bean;
   import org.springframework.context.annotation.Configuration;
   import org.togglz.core.repository.StateRepository;
   import org.togglz.core.user.NoOpUserProvider;
   import org.togglz.core.user.UserProvider;
   import org.togglz.mongodb.MongoStateRepository;
   import com.mongodb.client.MongoClient;

   @Configuration
   public class ToggleConfig {
       private static final String FEATURE_TOGGLE_TABLE = "feature_toggles";

       @Bean
       public UserProvider userProvider() {
           return new NoOpUserProvider();
       }

       @Bean
       public StateRepository getStateRepository(MongoClient mongoClient) {
           return MongoStateRepository.newBuilder(mongoClient, "demo")
                   .collection(FEATURE_TOGGLE_TABLE)
                   .build();
       }
   }
   ```

5. **Secure the REST API**
   Since the REST API allows programmatic control of feature flags, you should secure it to prevent unauthorized access. You can integrate Spring Security to restrict access to admin users:
   ```java
   import org.springframework.context.annotation.Configuration;
   import org.springframework.security.config.annotation.web.builders.HttpSecurity;
   import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
   import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

   @Configuration
   @EnableWebSecurity
   public class SecurityConfig extends WebSecurityConfigurerAdapter {
       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http.authorizeRequests()
               .antMatchers("/api/features/**").hasRole("ADMIN")
               .and().httpBasic()
               .and().csrf().disable();
       }
   }
   ```
   Additionally, include the `togglz-spring-security` dependency:
   ```xml
   <dependency>
       <groupId>org.togglz</groupId>
       <artifactId>togglz-spring-security</artifactId>
       <version>3.1.2</version>
   </dependency>
   ```

6. **Test the API**
   After starting your Spring Boot application, you can test the API:
   - **Get all features**: `curl http://localhost:8080/api/features`
     - Response: `FEATURE_ONE is enabled\nFEATURE_TWO is disabled`
   - **Get specific feature**: `curl http://localhost:8080/api/features/FEATURE_ONE`
     - Response: `{"name":"FEATURE_ONE","active":true}`
   - **Toggle a feature**: `curl -d '{"name":"FEATURE_ONE","active":false}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/features/FEATURE_ONE`
     - Response: `{"name":"FEATURE_ONE","active":false}`

### When to Use the Actuator Endpoint vs. Custom REST API
- **Use the Actuator Endpoint** if:
  - You need a quick, out-of-the-box solution for basic feature flag management.
  - You’re already using Spring Boot Actuator and want to integrate Togglz with your existing monitoring setup.
  - You only need simple GET/POST operations to view or toggle feature states.
- **Use a Custom REST API** if:
  - You need more control over the API’s structure, response format, or endpoints (e.g., custom DTOs, additional metadata, or complex toggling logic).
  - You want to integrate feature flag management with other APIs or external systems.
  - You need advanced security, validation, or user-specific toggle strategies beyond what the actuator provides.

### Additional Considerations
- **Persistence**: By default, Togglz uses an in-memory `StateRepository`, which doesn’t persist feature states across restarts. For production, consider using a persistent repository like `JDBCStateRepository`, `MongoStateRepository`, or `RedisStateRepository` to store feature states reliably.
- **Activation Strategies**: Togglz supports various activation strategies (e.g., `UsernameActivationStrategy`, `GradualActivationStrategy`). You can extend your REST API to configure these strategies programmatically.
- **Scalability**: If running multiple application instances, ensure the `StateRepository` is shared (e.g., via a database or Redis) to keep feature states consistent across instances.
- **Security**: Always secure the Togglz console and your custom REST API in production to prevent unauthorized access. Use Spring Security or similar mechanisms.

### Conclusion

While Togglz provides an actuator endpoint that offers basic REST-like functionality for managing feature flags, it does not include a fully-fledged REST API out of the box. Implementing a custom REST API is straightforward using the `FeatureManager` and Spring Boot’s REST capabilities, as shown in the example above. This approach gives you full control over the API’s design and allows integration with your application’s specific requirements. For further details, you can refer to the Togglz documentation (http://www.togglz.org) or community resources like the ones cited below.[](https://dev.to/chanuka/implementing-feature-flag-management-in-your-spring-boot-application-using-api-calls-and-ui-with-53a9)[](https://medium.com/tuanhdotnet/methods-for-implementing-feature-flag-management-in-your-spring-boot-application-02d38811a58b)[](https://www.linkedin.com/pulse/feature-toggle-implementation-using-spring-boot-jonnabhatla)

---

[Prev](./0009-distributed-tracing.md) | [Next](./0011-green-and-blue-deployment-strategy.md)
