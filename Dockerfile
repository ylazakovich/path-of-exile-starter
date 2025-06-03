FROM eclipse-temurin:21.0.7_6-jdk@sha256:5e7983a70405b3e40f79592276e73c81215fd9822bf37a66e325fb87a6faa57d AS builder
ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY modules/${MODULE} ./modules/${MODULE}

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:21.0.7_6-jre@sha256:a01533f7bebe415231fa525a20afd51747074afaf1d18140f3d9c7d4ff6d08a1
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
