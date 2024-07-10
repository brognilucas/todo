FROM openjdk:21-jdk-slim

WORKDIR /app

COPY gradlew /app/
COPY gradlew.bat /app/
COPY gradle /app/gradle

COPY build.gradle.kts /app/
COPY settings.gradle.kts /app/

COPY src /app/src

RUN chmod +x gradlew

RUN ./gradlew build -x test --no-daemon

RUN ./gradlew shadowJar --no-daemon

COPY build/libs/*-all.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
