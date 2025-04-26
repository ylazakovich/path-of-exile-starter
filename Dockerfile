FROM eclipse-temurin:17-jdk AS builder

ARG MODULE
WORKDIR /app

COPY lombok.config ./
COPY config ./config
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle

COPY modules/${MODULE} ./modules/${MODULE}

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
ARG MODULE
ARG PORT=8080
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
