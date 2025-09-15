# ==========================
#  Etapa 1: Build con Gradle
# ==========================
FROM gradle:8.14.3-jdk17-alpine AS build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de build
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Descargar dependencias
RUN ./gradlew dependencies --no-daemon || return 0

# Copiar el código fuente
COPY . .

# Compilar proyecto y generar jar
RUN ./gradlew clean bootJar --no-daemon

# ==========================
#  Etapa 2: Imagen final
# ==========================
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copiar el jar desde la etapa anterior
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
