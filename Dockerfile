FROM eclipse-temurin:21.0.7_6-jdk@sha256:784aa1a3cbbd6217307dded1749f85bccade79ffd539b771bbe6c4c94f60d593 AS builder
ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY modules/${MODULE} ./modules/${MODULE}
COPY shared ./shared

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:21.0.7_6-jre@sha256:bca347dc76e38a60a1a01b29a7d1312e514603a97ba594268e5a2e4a1a0c9a8f
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
