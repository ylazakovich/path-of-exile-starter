FROM eclipse-temurin:21.0.7_6-jdk@sha256:b997045cddee5aa5460aaec871775d8f24e5bea1af1592d8741bd5d4ff793e27 AS builder
ARG MODULE
WORKDIR /app

COPY gradlew settings.gradle build.gradle lombok.config ./
COPY gradle ./gradle
COPY config ./config
COPY modules/${MODULE} ./modules/${MODULE}

ENV ORG_GRADLE_JAVA_INSTALLATIONS_AUTO_DETECT=true

RUN ./gradlew modules:${MODULE}:bootJar -x test --no-daemon

FROM eclipse-temurin:21.0.7_6-jre@sha256:f08ebc4aae836b96ec861a89b5187260a9bca54ad87255ca55eddbf097b444f5
ARG MODULE
ARG PORT
WORKDIR /app

COPY --from=builder /app/modules/${MODULE}/build/libs/*.jar app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", "-jar", "app.jar"]
