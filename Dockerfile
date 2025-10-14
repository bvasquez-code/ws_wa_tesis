# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml ./
# Copia settings si lo usas (opcional)
COPY .mvn .mvn
RUN mvn -q -DskipTests --no-transfer-progress dependency:go-offline

COPY src ./src
RUN mvn -q -DskipTests --no-transfer-progress clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

ENV JAVA_OPTS=""
EXPOSE 8080
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]