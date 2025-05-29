plugins {
    java
    alias(libs.plugins.spring.boot)
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

    testImplementation(libs.spring.boot.starter.test)

    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
