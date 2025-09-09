FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

ADD https://dtdg.co/latest-java-tracer dd-java-agent.jar

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV DD_SERVICE=my-api-service
ENV DD_ENV=production
ENV DD_VERSION=1.0.0
ENV DD_LOGS_INJECTION=true
ENV DD_TRACE_ENABLED=true
ENV DD_AGENT_HOST=localhost
ENV DD_TRACE_AGENT_PORT=8126

ENTRYPOINT ["java", "-javaagent:/app/dd-java-agent.jar", "-jar", "app.jar"]