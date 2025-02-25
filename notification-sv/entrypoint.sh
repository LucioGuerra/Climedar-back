#!/bin/sh
set -e

# Verifica que el archivo del certificado exista en /etc/secrets
if [ ! -f /etc/secrets/ca-cert.crt ]; then
  echo "Error: El certificado CA no se encuentra en /etc/secrets/ca-cert.crt"
  exit 1
fi

# Genera el truststore a partir del certificado, si aún no existe
if [ ! -f "$KAFKA_TRUSTSTORE_LOCATION" ]; then
  echo "Generando truststore en $KAFKA_TRUSTSTORE_LOCATION..."
  keytool -import -alias ca-cert -file /etc/secrets/ca-cert.crt -keystore "$KAFKA_TRUSTSTORE_LOCATION" -storepass "$KAFKA_TRUSTSTORE_PASSWORD" -noprompt
else
  echo "El truststore ya existe en $KAFKA_TRUSTSTORE_LOCATION, se omite la generación."
fi

echo "Iniciando la aplicación..."
exec java -jar app.jar
