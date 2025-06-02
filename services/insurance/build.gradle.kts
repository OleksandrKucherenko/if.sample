plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.sample.insurance"
version = "0.0.1-SNAPSHOT"

// Java version, v24 
java.sourceCompatibility = JavaVersion.VERSION_24

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(24)
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)

    // WebFlux for reactive programming, Non-blocking
    implementation(libs.spring.boot.starter.webflux)

    // Health Checks, Metrics, etc
    implementation(libs.spring.boot.starter.actuator) 

    // OpenAPI with SpringDoc
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.bundles.jackson)
    implementation(libs.spring.boot.starter.validation)

    // Structured Logging with Logstash
    implementation(libs.logstash.logback.encoder)
    
    // Distributed Tracing with OpenTelemetry
    implementation(libs.spring.boot.starter.opentelemetry)
    implementation(libs.micrometer.tracing.bridge.otel)
    
    // Feature Toggle with Togglz and Redis
    implementation(libs.bundles.togglz)
    implementation(libs.togglz.security)
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.jedis)
    
    // Spring Security for Togglz admin console, swagger and api (basic auth)
    implementation(libs.spring.boot.starter.security)
    
    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
    
    // TestContainers, e2e tests
    testImplementation(libs.bundles.testcontainers)
    testImplementation(libs.togglz.junit)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
