#boşluk bırakarak yazmalıyız
FROM eclipse-temurin:17-jdk-alpine

LABEL authors="akif"

EXPOSE 8080

COPY target/user2-0.0.1-SNAPSHOT.jar user2-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "user2-0.0.1-SNAPSHOT.jar"]

