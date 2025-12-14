FROM maven:3.8.7-openjdk-17 AS builder
WORKDIR /app

# Copy maven wrapper and pom to leverage cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x ./mvnw

# Copy source and build
COPY src ./src
RUN ./mvnw -B -DskipTests package

# Runtime image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy built jar (use wildcard to avoid hardcoding artifact name)
COPY --from=builder /app/target/*.jar app.jar

# Default server port for this service
ENV SERVER_PORT=8081
EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar --server.port=${SERVER_PORT}"]
