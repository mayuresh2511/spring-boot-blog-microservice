FROM eclipse-temurin:17-jdk-jammy as base
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app.jar"]

FROM base as test
CMD ["./mvnw", "test"]

FROM base as development
ENTRYPOINT ["java","-jar","/app.jar"]