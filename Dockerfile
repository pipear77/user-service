FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY applications/app-service/build/libs/user-service.jar user-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "user-service.jar"]
