# Etapa de construcción
FROM gradle:8.5-jdk17-alpine AS builder
WORKDIR /app
COPY . .
# Construye sin ejecutar tests para más rapidez
RUN gradle build -x test --no-daemon

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia el JAR construido
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
