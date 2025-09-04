# ---------- Build stage ----------
FROM eclipse-temurin:21.0.8_9-jdk@sha256:34579903d57acb954e7bc6eadf4337fa9f99d1f5ed01129ceb8a12863bf91f8e AS builder

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
FROM eclipse-temurin:21.0.8_9-jre@sha256:19ec9faa4bbefbb9538ff237d08d24853ea9ce97bd3213b609be1dcbb76757c6

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

USER 1000

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
