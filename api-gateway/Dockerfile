FROM docker.io/eclipse-temurin:17-jdk-alpine

WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MinRAMPercentage=50.0 -XX:MaxRAMPercentage=80.0"

EXPOSE 443

CMD ["java", "-jar", "app.jar"]