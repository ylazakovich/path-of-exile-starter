# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25-jdk@sha256:3a35a62d3ece346cfde11b08fafdc3f99d3946d31d5ebc15d678b44a67c3b9ec AS builder

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
FROM busybox:1.37.0@sha256:2f590fc602ce325cbff2ccfc39499014d039546dc400ef8bbf5c6ffb860632e7 AS bb

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jre@sha256:6f9495321d69d131c0cd32ad9bf83cb8cc21ce5ca9a9ab234e8d5596d0cc02d2

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
