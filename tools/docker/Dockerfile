# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25-jdk@sha256:18755606ff20b3871244a75b026eaf5513b93739974100be9d86362f9bc4f98b AS builder

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
FROM eclipse-temurin:25-jre@sha256:014b951b8f1e7c723e008f31a447068fac48d316e9aff1c586ef1f032529533d

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
