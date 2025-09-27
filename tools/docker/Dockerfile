# ---------- Build stage ----------
FROM --platform=$BUILDPLATFORM eclipse-temurin:25-jdk@sha256:21f6c51087c4fa7775b802c9d2ca7e3319eedf32e95ee94cac44a6d0f543a83f AS builder

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
FROM busybox:1.37.0@sha256:d82f458899c9696cb26a7c02d5568f81c8c8223f8661bb2a7988b269c8b9051e AS bb

# ---------- Runtime stage ----------
FROM eclipse-temurin:25-jre@sha256:74d5c631e5db5a44e7f5a2dd49f93f0c6f7b8c22c1dc1b8e1caec7009872c5c3

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=bb /bin/busybox /bin/busybox

# jar
COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar /app/app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
