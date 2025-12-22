# Build stage
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY backend/pom.xml backend/pom.xml
COPY backend/src backend/src
RUN mvn -f backend/pom.xml -DskipTests package

# Run stage
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /workspace/backend/target/*-SNAPSHOT.jar /app/app.jar

ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java -Djava.net.preferIPv4Stack=true -jar /app/app.jar --server.port=${PORT}"]
