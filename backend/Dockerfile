# First stage: build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Second stage: run the application
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar demo.jar
ENTRYPOINT ["java", "-jar", "demo.jar"]



