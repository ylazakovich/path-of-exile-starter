FROM eclipse-temurin:17.0.15_6-jdk@sha256:324122d7274b03f0b0ea58b46c4dbea15f1861a1d543547b6bc9c076ac730b9f AS builder

ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config

COPY modules/${MODULE} ./modules/${MODULE}

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:17.0.15_6-jre@sha256:7b62d52c4978a88a7b2d096a3e27cd961f943407825d75c8db6260812ec13eec
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
