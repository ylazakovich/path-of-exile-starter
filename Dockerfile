FROM eclipse-temurin:21.0.7_6-jdk@sha256:2cc80b4288f6240734ecd1acae01d20afa56f86327b9ae207a9468363422c974 AS builder
ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY modules/${MODULE} ./modules/${MODULE}
COPY shared ./shared

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:21.0.7_6-jre@sha256:ffed502a66e8be6e27b6317fcd5913e967dd79e1e2f5135b2c1ffce557930ec3
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
