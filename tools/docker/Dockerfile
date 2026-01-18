# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25.0.1_8-jdk@sha256:10331564d9ae41b6a534ddea472f37270a3c286e89857261631a0d772a4d8617 AS builder

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
FROM busybox:1.37.0@sha256:2383baad1860bbe9d8a7a843775048fd07d8afe292b94bd876df64a69aae7cb1 AS bb

# ---------- Runtime stage ----------
FROM eclipse-temurin:25.0.1_8-jre@sha256:96bccca442e8842a7366df338f5960e3342862ea6b8d42b97009a3dfd8602547

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
