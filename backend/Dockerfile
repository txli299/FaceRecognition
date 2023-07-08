FROM maven:3.8.6-openjdk-18 as builder
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package
 
FROM openjdk:18-oracle
WORKDIR /app
COPY --from=builder /usr/src/app/target/backend-1.0.jar /app/backend-1.0.jar
ENV SERVER_PORT 80
EXPOSE 80
CMD ["java", "-jar", "/app/backend-1.0.jar"]