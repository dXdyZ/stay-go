FROM gradle:8.4-jdk17-alpine AS build
WORKDIR /app

COPY settings.gradle .
COPY build.gradle .
COPY src ./src

RUN gradle clean build --no-daemon

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring \
    && chown -R spring:spring /app \
    && chmod -R 755 /app

USER spring:spring

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 9393

ENTRYPOINT exec java ${JAVA_OPTS} -jar /app/app.jar






