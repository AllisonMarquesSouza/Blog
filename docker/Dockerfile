FROM openjdk:17-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /usr/local/app

# Copy the Maven build files and source code
COPY ../pom.xml ./
COPY ../src ./src

# Install Maven and build the application
RUN apt-get update && \
    apt-get install -y maven && \
    mvn clean package -DskipTests

# Use a smaller base image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /usr/local/app

# Copy the built JAR file from the builder stage
COPY --from=builder /usr/local/app/target/*.jar app.jar

# Expose the port that your Java application runs on
EXPOSE 8080

# Run the Java application
CMD ["java", "-jar", "app.jar"]
