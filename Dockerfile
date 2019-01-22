FROM openjdk:11-jre-stretch AS base

FROM maven:3-jdk-11-slim AS build
WORKDIR /build
COPY . .
RUN mvn clean compile package

FROM base AS run
WORKDIR /java
COPY --from=build /build/target/Telegram-TheAyyBot-1.0-jar-with-dependencies.jar ./bot.jar

ENTRYPOINT ["java", "-jar", "bot.jar"]
