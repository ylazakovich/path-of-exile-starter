# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25.0.3_9-jdk@sha256:c2b7ea21649875fb9052237ac4e3cd4ef63968a2a389a0a1b1a72a5e53e5c93f AS builder

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

# ---------- Busybox stage ----------
FROM busybox:1.38.0@sha256:b6762ddf4a50aabb5f4d21aa6f447d05d5633fb09f09c08b33f22356a2f98be0 AS bb

# ---------- Runtime stage ----------
FROM eclipse-temurin:25.0.3_9-jre@sha256:04262e8782d6b034ee5d7c1c5d4e8938fcf2063a76b4bfcd84e5d994d09c27bc

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
