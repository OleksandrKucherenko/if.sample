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
    implementation(libs.spring.boot.starter.webflux)
    
    // OpenAPI documentation
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
    implementation(libs.bundles.jackson)
    
    // For validation
    implementation(libs.spring.boot.starter.validation)
    
    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
