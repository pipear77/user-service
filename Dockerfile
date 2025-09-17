FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
EXPOSE 8080
COPY applications/app-service/build/libs/user-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
