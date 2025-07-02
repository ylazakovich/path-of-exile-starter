FROM eclipse-temurin:21.0.7_6-jdk@sha256:1c37779fe2f338d42a7bc8ac439920ef2bf7cebb7deb0970f5733219b17e9868 AS builder
ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY modules/${MODULE} ./modules/${MODULE}
COPY shared ./shared

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:21.0.7_6-jre@sha256:031eb6ccad828004407c0a41f6a4009a08289961d55e9119287c28756522a945
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
