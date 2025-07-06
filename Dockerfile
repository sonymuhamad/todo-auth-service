# ---------- Build stage ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests

# ---------- Runtime stage ----------
FROM openjdk:8-jdk-alpine
WORKDIR /app

# JAR_FILE passed from Makefile (defaults to the only jar in /target)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose REST (8080) and gRPC (9090)

ENTRYPOINT ["java","-jar","app.jar"]
