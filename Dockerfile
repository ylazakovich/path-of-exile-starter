FROM eclipse-temurin:21.0.7_6-jdk@sha256:2d101f7d06beedb058a34ddd75a8da0784c998d584d1ef78471dd8294bd9a77c AS builder
ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY modules/${MODULE} ./modules/${MODULE}
COPY shared ./shared

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:21.0.7_6-jre@sha256:28bede20f9b759d5a07065d42fe19917286f0bbe596a5935bd000a7869644374
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
