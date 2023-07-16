# Use the official gradle/Java 11 image to create a build artifact.
# https://hub.docker.com/_/gradle
FROM gradle:7.2.0-jdk11 AS build

WORKDIR /home/gradle/project

# Copy the build.gradle.kts file to the Docker image
COPY build.gradle.kts .

# Copy the src directory (Java source code) to the Docker image
COPY src ./src

# Build the application
RUN gradle clean build

# Use OpenJDK 11 to run the application
FROM openjdk:11-jdk

# Copy the jar file from the build stage to the production image
COPY --from=build /home/gradle/project/build/libs/*.jar /usr/app/RewardsCentral.jar

# Set the working directory
WORKDIR /usr/app

# Run the application
CMD ["java", "-jar", "RewardsCentral.jar"]
