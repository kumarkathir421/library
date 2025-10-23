# =======================================
# Stage 1 — Build the JAR using Maven
# =======================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# =======================================
# Stage 2 — Run the Spring Boot app
# =======================================
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the generated JAR from the first stage
COPY --from=build /app/target/library-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
