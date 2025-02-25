# Stage 1: Construcción del proyecto con Maven
FROM maven:3.9.9-ibm-semeru-17-focal AS builder
WORKDIR /build
# Copia el descriptor de Maven y el código fuente
COPY pom.xml .
COPY src ./src
# Compila el proyecto y empaqueta el JAR (se omiten los tests para acelerar el build)
RUN mvn clean package -DskipTests

# Stage 2: Imagen de ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el archivo truststore base (si se requiere, o se puede omitir para crearlo desde cero)
# En este ejemplo, se generará el truststore en tiempo de ejecución, por lo que no se copia previamente.
# COPY truststore.jks /app/truststore.jks

# Copia el JAR generado en el stage anterior (se asume que se genera en "target")
COPY --from=builder /build/target/*.jar /app/app.jar

# Copia el script de entrypoint al contenedor
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Expone el puerto en el que la aplicación escucha
EXPOSE 8090



# Usa el script de entrypoint para generar el truststore y luego iniciar la aplicación
ENTRYPOINT ["/app/entrypoint.sh"]
