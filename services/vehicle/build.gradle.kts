plugins {
    java
    alias(libs.plugins.spring.boot)
}

group = "com.sample.vehicle"
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

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.bundles.mockito)

    testRuntimeOnly(libs.junit.platform.launcher)
    testRuntimeOnly(libs.bundles.mockito.agent)

    mockitoAgent(libs.mockito.core) { isTransitive = false }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}
