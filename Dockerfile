# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# 1) Copiamos mvnw y aseguramos permisos +X (y quitamos CRLF si vienen de Windows)
COPY mvnw ./
RUN chmod +x mvnw \
 && sed -i 's/\r$//' mvnw

# 2) Pom y configuración offline
COPY .mvn .mvn
COPY pom.xml ./
RUN ./mvnw -q -DskipTests dependency:go-offline

# 3) Código fuente y empaquetado
COPY src src
RUN ./mvnw -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# 4) Copiamos el JAR generado
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/data/tesis

# 5) Opciones JVM y puerto
ENV JAVA_OPTS=""
EXPOSE 8080

# 6) Arranque
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]