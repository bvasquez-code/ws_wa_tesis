# ---- Build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copia el wrapper y el pom primero para aprovechar cache
COPY mvnw ./
# Asegura permisos de ejecución y elimina CRLF por si viene de Windows
RUN chmod +x mvnw \
 && sed -i 's/\r$//' mvnw

COPY .mvn .mvn
COPY pom.xml ./

# Descarga dependencias en modo offline
RUN ./mvnw -q -DskipTests dependency:go-offline

# Ahora copia el resto del código fuente
COPY src src

# Empaqueta la aplicación
RUN ./mvnw -q -DskipTests clean package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usa el JAR generado (ajusta el nombre si cambia)
COPY --from=build /app/target/app-0.0.1-SNAPSHOT.jar app.jar

# Variable opcional para activar perfiles, ajustes de JVM, etc.
ENV JAVA_OPTS=""

# Railway expone el puerto que necesites; por convención usamos 8080
EXPOSE 8080

# Lanza la aplicación
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]