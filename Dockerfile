FROM maven:11-jdk-slim AS base

FROM openjdk:11-jre-stretch AS build
WORKDIR /build
COPY . .
RUN mvn clean compile jar

FROM base AS run
WORKDIR /java
COPY --from=build /build/target/Telegram-TheAyyBot-1.0-jar-with-dependencies.jar ./bot.jar

ENTRYPOINT ["java", "-jar", "bot.jar"]
