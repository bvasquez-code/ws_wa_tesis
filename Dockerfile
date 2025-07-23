# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia el wrapper y el pom primero para aprovechar cache
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw -q -DskipTests dependency:go-offline

# Ahora copia el resto del código
COPY src src

# Empaqueta
RUN ./mvnw -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usa el jar generado (ajusta el nombre si cambia)
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Variable para perfiles, etc.
ENV JAVA_OPTS=""

# Railway setea PORT, expón uno cualquiera
EXPOSE 8080

# Lanza
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]