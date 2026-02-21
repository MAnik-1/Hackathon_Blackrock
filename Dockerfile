# docker build -t blk-hacking-ind-manikanta-k .

# Using lightweight Linux-based OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 5477

ENTRYPOINT ["java", "-jar", "app.jar"]
