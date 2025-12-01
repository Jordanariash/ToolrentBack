# Etapa de construcción
FROM gradle:8.5-jdk17-alpine AS builder
WORKDIR /app
COPY . .
# Construye sin ejecutar tests para más rapidez
RUN gradle build -x test --no-daemon

# Etapa de ejecución
FROM openjdk:17-jdk-alpine
WORKDIR /app
# Copia el JAR construido
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
