# 6. Dockerize the Vehicle Service

Date: 2025-05-29

## Status

Accepted

## Context

To deploy the vehicle service in a container, we need to dockerize it.

## Decision

Use Docker to containerize the vehicle service.

```bash
# Spring Boot JVM image (default)
./gradlew services:vehicle:bootBuildImage

# show the image details
docker image inspect vehicle:0.0.1-SNAPSHOT | jq

docker image ps 
# Expected output:
# REPOSITORY  TAG              SIZE
# vehicle     0.0.1-SNAPSHOT   145MB

# run the container build by gradle plugin
docker run -p 50080:50080 vehicle:0.0.1-SNAPSHOT
# Expected output:
# Starting AOT-processed VehicleApplication

# validate endpoint
http http://localhost:50080/vehicles/nmg52y # expected OK 200

# to examime the Docker Image run DIVE tool
dive vehicle:0.0.1-SNAPSHOT
```

It is also possible to build the image manually:

```bash
# ask gradle to build regular JVM Docker image (default, native=OFF)
./gradlew services:vehicle:build
# Ensure build/libs/vehicle-0.0.1-SNAPSHOT.jar exists

# change directory to the vehicle service to provide a proper Docker build scope
cd services/vehicle

# build the Docker image
docker build -t vehicle-service:latest .

# run the container
docker run -p 50080:50080 vehicle-service:latest
# Expected output:
# Starting VehicleApplication v0.0.1-SNAPSHOT using Java 24.0.1

# validate endpoint
http http://localhost:50080/vehicles/nmg52y # expected OK 200
```

Important to have in `.dockerignore` line:

```.dockerignore
!build/libs/*.jar
```

### Native Images, GraalVM

```bash
# installation of the GraalVM required
brew install --cask graalvm-jdk

# list all installed Java runtime versions
/usr/libexec/java_home -V
# Expected:
# 24.0.1 (arm64) "Oracle Corporation" - "Oracle GraalVM 24.0.1+9.1" /Library/Java/JavaVirtualMachines/graalvm-24.jdk/Contents/Home

# add GraalVM to jenv known versions
jenv add /Library/Java/JavaVirtualMachines/graalvm-24.jdk/Contents/Home

# add this into .envrc or simply run in the terminal
export GRAALVM_HOME="$(jenv prefix oracle64-24.0.1)"
```

```bash
# ALTERNATIVE: to explicitly build native image (by default it is disabled)
BP_NATIVE_IMAGE=true ./gradlew services:vehicle:bootBuildImage

# Expected output:
# Starting AOT-processed VehicleApplication
```

#### Build and Run the Optimized Native Image

```bash
# 1. Build the native executable with Spring Boot (requires GraalVM and Docker):
./gradlew nativeCompile

# to execut the native tests
./gradlew nativeTest

# 2. Build the optimized native Docker image (VERY SLOW!!!!)
docker build -f services/vehicle/Dockerfile.native -t vehicle:native .

# 3. Run the native container (port 50080)
docker run --rm -p 50080:50080 vehicle:native

# 4. Test the running service endpoint
http http://localhost:50080/vehicles/nmg52y # expected HTTP 200 OK
```

be sure that `.dockerignore` contains the exception for accessing the native binary.

```.dockerignore
!build/native/nativeCompile/vehicle
```

## Consequences

- Native docker image size is smaller than JVM docker image size (2 times smaller)
- Native docker image is faster to start and use less memory
- For docker image examining can be used `dive` tool
- Native image compression can be done by tool UPX, use `BP_BINARY_COMPRESSION_METHOD=upx` to enable compression

```bash
# https://formulae.brew.sh/formula/upx
brew install upx

# Original Size: 105_524_616 (105mb)

# compression, very SLOW
upx --best services/vehicle/build/native/nativeCompile/vehicle --force-macos # ration 36% left

# a little faster (40_353_808 - 40mb)
upx -9 services/vehicle/build/native/nativeCompile/vehicle --force-macos # ration 38% left

# fast (41_156_624 - 41mb)
upx -7 services/vehicle/build/native/nativeCompile/vehicle --force-macos # ration 39% left

# fastest (42_729_488 - 42.5mb)
upx -5 services/vehicle/build/native/nativeCompile/vehicle --force-macos # ration 41% left

# decompression
upx -d services/vehicle/build/native/nativeCompile/vehicle
```

## Troubleshooting

### Gradle Issues

```bash
./gradlew --stop
```

### Out Of Memory During Native Image Building

```bash
# restart Colima with higher memory and cpu resources
colima stop && colima start --cpu 4 --memory 8
colima list # to check the allocated resources
# alternative: edit Colima template `colima template`
```

### Execute Native Image on Alpine Linux (unstable)

To reduce chances of failure we should apply `--static` (link all libaries statically) 
and `--libc=musl` (use musl libc instead of glibc). After that is possible to execute
native binary on Alpine Linux.

This section is required in `services/vehicle/build.gradle.kts`:

```kotlin
graalvmNative {
    binaries {
        named("main") {
            val os = System.getProperty("os.name").lowercase()
            if (os.contains("linux")) {
                buildArgs.add("--static")
                buildArgs.add("--libc=musl")
            } else {
                println("Skipping --static and --libc=musl for non-Linux OS: $os")
            }
        }
    }
}
```

### Issues during docker image building

Error: 

```text
0.087 xargs is not available
```

Add this to `Dockerfile.native`:

```Dockerfile
RUN microdnf -y install findutils
```

## References

- https://www.graalvm.org/latest/reference-manual/native-image/guides/containerise-native-executable-and-run-in-docker-container/
- https://github.com/graalvm/graalvm-demos/tree/master/native-image/spring-boot-microservice-jibber
- https://paketo.io/docs/reference/java-native-image-reference/ - Java Native Image Buildpack Reference

## Examples

- https://github.com/josephrodriguez/springboot-starterkit/blob/main/Dockerfile.aot
- https://github.com/hexagontk/real_world/blob/main/backend/native.dockerfile
- https://github.com/lutergs-dev/backend/blob/main/Dockerfile-GraalVM
- https://github.com/nzuguem/java-native-compilation-workshop/tree/main/03-springboot

### Find nativeCompile samples on github

- https://github.com/search?q=nativeCompile%20path%3ADockerfile&type=code
- https://github.com/search?q=%22ghcr.io%2Fgraalvm%22+path%3ADockerfile+language%3ADockerfile+gradle&type=code
