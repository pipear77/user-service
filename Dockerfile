FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
