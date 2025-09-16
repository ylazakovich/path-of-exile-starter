# ---------- Build stage ----------
FROM eclipse-temurin:21.0.8_9-jdk@sha256:2b9cd724df3c0797032bfcfebd268ba89e80a77e93c791c4cecd4d5e1202e027 AS builder

ARG MODULE
ARG GRADLE_OPTS
ENV GRADLE_OPTS=${GRADLE_OPTS}
WORKDIR /app

COPY gradlew gradle.properties build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY tools/docker/gradle/settings-${MODULE}.gradle settings.gradle

COPY modules/${MODULE} ./modules/${MODULE}
COPY shared ./shared

RUN ./gradlew modules:${MODULE}:bootJar --no-daemon --build-cache -x test

# ---------- Runtime stage ----------
FROM eclipse-temurin:21.0.8_9-jre@sha256:bc03ecb547faf36e4ca8a12686f14eb0f7ffcd8b402c45966ad1d051ebb3d3e3

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

USER 1000

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
