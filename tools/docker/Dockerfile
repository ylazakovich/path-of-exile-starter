# ---------- Build stage ----------
FROM eclipse-temurin:21.0.8_9-jdk@sha256:4d8eddfd7ce6ce35c6a42115d73ef7e92a3e112fddbae21a6e273d2e05fc9fff AS builder

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
FROM eclipse-temurin:21.0.8_9-jre@sha256:49f7a6b514f9f72fd27d178abd553dde1e0430c3508a4f00f3a7d605a4c76b68

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

USER 1000

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
