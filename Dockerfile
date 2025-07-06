FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY target/*.jar app.jar

# Expose REST (8080) and gRPC (9090)

ENTRYPOINT ["java","-jar","app.jar"]
