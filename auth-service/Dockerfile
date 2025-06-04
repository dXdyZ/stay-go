FROM gradle:8.4-jdk17-alpine AS build

WORKDIR /app

COPY settings.gradle .
COPY build.gradle .
COPY src ./src

#Убрать пропуск тестов в продакшене
RUN gradle clean build --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN mkdir -p /app/secrets && \
    groupadd -r spring && useradd -r -g spring spring \
    && chown -R spring:spring /app \
    && chmod -R 755 /app

USER spring:spring

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 9191

ENTRYPOINT exec java ${JAVA_OPTS} -jar /app/app.jar