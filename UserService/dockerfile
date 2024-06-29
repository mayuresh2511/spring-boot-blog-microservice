FROM eclipse-temurin:17-jdk-jammy as base
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9092

FROM base as test
CMD ["./mvnw", "test"]

FROM base as development
#CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=mysql", "-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=DEV","/app.jar"]