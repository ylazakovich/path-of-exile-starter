# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25.0.1_8-jdk@sha256:42fc3fe6804ec612f5ef8a613f8c06d8dd578de6207336077387d4cb32edaa9b AS builder

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
FROM busybox:1.37.0@sha256:b3255e7dfbcd10cb367af0d409747d511aeb66dfac98cf30e97e87e4207dd76f AS bb

# ---------- Runtime stage ----------
FROM eclipse-temurin:25.0.1_8-jre@sha256:9d1d3068b16f2c4127be238ca06439012ff14a8fdf38f8f62472160f9058464a

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
