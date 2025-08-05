# ---------- Build stage ----------
FROM eclipse-temurin:21.0.8_9-jdk@sha256:1f3171966c0ca6b7151c56059bc70c40d3b880b4333feb91d5b09a68f159af3b AS builder

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
FROM eclipse-temurin:21.0.8_9-jre@sha256:1297ef4131440ba9d8c17335fa27969e76550acb72b648eed7618d93bec98f98

ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

USER 1000

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
