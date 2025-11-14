# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25.0.1_8-jdk@sha256:d776c7ab7f1bc0eb4d1ebaddae76a027408c63422932fdda665714a58de75bb9 AS builder

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
FROM busybox:1.37.0@sha256:e3652a00a2fabd16ce889f0aa32c38eec347b997e73bd09e69c962ec7f8732ee AS bb

# ---------- Runtime stage ----------
FROM eclipse-temurin:25.0.1_8-jre@sha256:daeb27e2851e570a11b6d117054778617ef3c7160f9ef77293a6d25e5c0f27ea

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
