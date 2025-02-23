#!/bin/bash

set -a
source .env
set +a

set -e

# Función para esperar que un contenedor ejecute correctamente su “health check”
# Recibe:
#   $1 -> nombre del contenedor
#   $2 -> comando de salud a ejecutar dentro del contenedor (se evaluará con sh -c)
#   $3 -> número máximo de intentos (opcional, por defecto 10)
#   $4 -> intervalo entre intentos en segundos (opcional, por defecto 20)
wait_for() {
    local container="$1"
    local health_cmd="$2"
    local max_attempts="${3:-20}"
    local interval="${4:-10}"
    echo "Esperando que $container se ponga saludable..."
    for i in $(seq 1 "$max_attempts"); do
        if podman exec "$container" sh -c "$health_cmd" > /dev/null 2>&1; then
            echo "$container está saludable."
            return 0
        else
            echo "[$i/$max_attempts] $container no está listo, reintentando en ${interval}s..."
            sleep "$interval"
        fi
    done
    echo "Error: $container no alcanzó estado saludable."
    exit 1
}

# Crear la red (si no existe)
if podman network exists hackacode; then
    podman network rm hackacode
fi

podman network create hackacode

##############################
# 1. Servicio MySQL
##############################
if podman container exists hackacode-db; then
    if podman ps --filter "name=hackacode-db" --format "{{.Names}}" | grep -w "hackacode-db" > /dev/null; then
        echo "MySQL (hackacode-db) ya está en ejecución. No se realizará ninguna acción."
    else
        echo "MySQL (hackacode-db) existe pero no está en ejecución. Reiniciando..."
        podman restart hackacode-db
    fi
else
    echo "Iniciando MySQL..."
    podman run -d \
      --name hackacode-db \
      --memory 512m \
      --restart=always \
      -e MYSQL_ROOT_PASSWORD=root \
      -p 3306:3306 \
      -v "./mysql/init:/docker-entrypoint-initdb.d" \
      -v "./mysql/data:/var/lib/mysql" \
      -v "./mysql/my.cnf:/etc/mysql/conf.d/my.cnf" \
      --network hackacode \
      docker.io/mysql:8.0
fi

# Espera a que MySQL responda (usa mysqladmin ping)
wait_for hackacode-db "mysqladmin ping -h localhost"

./build_all.sh

##############################
# 2. Servicio Eureka
##############################
echo "Compilando e iniciando eureka-sv..."
podman build -t eureka-sv ./eureka-sv
if podman container exists eureka-sv; then
    echo "El contenedor eureka-sv ya existe. Reiniciando..."
    podman restart eureka-sv
else
    podman run -d \
      --name eureka-sv \
      --memory 384m \
      --restart=always \
      -p 8761:8761 \
      -v "${EUREKA_DIR}/eureka-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      --network hackacode \
      eureka-sv
fi
wait_for eureka-sv "wget -qO- http://localhost:8761/actuator/health"

##############################
# 3. Servicios que dependen de MySQL y Eureka
##############################

echo "Compilando e iniciando doctor-sv..."
podman build -t doctor-sv ./doctor-sv
if podman container exists doctor-sv; then
    echo "El contenedor doctor-sv ya existe. Reiniciando..."
    podman restart doctor-sv
else
    podman run -d \
      --name doctor-sv \
      --memory 768m \
      --restart=always \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -e DB_URL=hackacode-db:3306 \
      -e DB_NAME=doctor_db \
      -e EUREKA_URL=eureka-sv:8761 \
      -e KAFKA_URL="${KAFKA_URL}" \
      -e KAFKA_PASSWORD="${KAFKA_PASSWORD}" \
      -e KAFKA_TRUSTSTORE_LOCATION="/app/certs/truststore.jks" \
      -e KAFKA_TRUSTSTORE_PASSWORD="${KAFKA_TRUSTSTORE_PASSWORD}" \
      -e KAFKA_USER="${KAFKA_USER}" \
      -e PROFILE=prod \
      -p 8083:8083 \
      -v "${DOCTOR_DIR}/doctor-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      -v "${KAFKA_TRUSTSTORE_LOCATION}:/app/certs/truststore.jks:ro" \
      --network hackacode \
      doctor-sv
fi

echo "Compilando e iniciando medical-service-sv..."
podman build -t medical-service-sv ./medical-service-sv
if podman container exists medical-service-sv; then
    echo "El contenedor medical-service-sv ya existe. Reiniciando..."
    podman restart medical-service-sv
else
    podman run -d \
      --name medical-service-sv \
      --memory 512m \
      --restart=always \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -e DB_URL=hackacode-db:3306 \
      -e DB_NAME=medical_service_db \
      -e EUREKA_URL=eureka-sv:8761 \
      -e PROFILE=prod \
      -p 8081:8081 \
      -v "${MEDICAL_DIR}/medical-service-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      --network hackacode \
      medical-service-sv
fi

wait_for doctor-sv "wget -qO- http://localhost:8083/actuator/health"
wait_for medical-service-sv "wget -qO- http://localhost:8081/actuator/health"

##############################
# 4. Servicios que dependen de Patient y MedicalServices
##############################

echo "Compilando e iniciando patient-sv..."
podman build -t patient-sv ./patient-sv
if podman container exists patient-sv; then
    echo "El contenedor patient-sv ya existe. Reiniciando..."
    podman restart patient-sv
else
    podman run -d \
      --name patient-sv \
      --memory 512m \
      --restart=always \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -e DB_URL=hackacode-db:3306 \
      -e DB_NAME=patient_db \
      -e EUREKA_URL=eureka-sv:8761 \
      -e KAFKA_URL="${KAFKA_URL}" \
      -e KAFKA_PASSWORD="${KAFKA_PASSWORD}" \
      -e KAFKA_TRUSTSTORE_LOCATION="/app/certs/truststore.jks" \
      -e KAFKA_TRUSTSTORE_PASSWORD="${KAFKA_TRUSTSTORE_PASSWORD}" \
      -e KAFKA_USER="${KAFKA_USER}" \
      -e PROFILE=prod \
      -p 8082:8082 \
      -v "${PATIENT_DIR}/patient-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      -v "${KAFKA_TRUSTSTORE_LOCATION}:/app/certs/truststore.jks:ro" \
      --network hackacode \
      patient-sv
fi

echo "Compilando e iniciando consultation-sv..."
podman build -t consultation-sv ./consultation-sv
if podman container exists consultation-sv; then
    echo "El contenedor consultation-sv ya existe. Reiniciando..."
    podman restart consultation-sv
else
    podman run -d \
      --name consultation-sv \
      --memory 768m \
      --restart=always \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -e DB_URL=hackacode-db:3306 \
      -e DB_NAME=consultation_db \
      -e EUREKA_URL=eureka-sv:8761 \
      -e KAFKA_URL="${KAFKA_URL}" \
      -e KAFKA_PASSWORD="${KAFKA_PASSWORD}" \
      -e KAFKA_TRUSTSTORE_LOCATION="/app/certs/truststore.jks" \
      -e KAFKA_TRUSTSTORE_PASSWORD="${KAFKA_TRUSTSTORE_PASSWORD}" \
      -e KAFKA_USER="${KAFKA_USER}" \
      -e PROFILE=prod \
      -p 8086:8086 \
      -v "${CONSULTATION_DIR}/consultation-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      -v "${KAFKA_TRUSTSTORE_LOCATION}:/app/certs/truststore.jks:ro" \
      --network hackacode \
      consultation-sv
fi

echo "Compilando e iniciando payment-sv..."
podman build -t payment-sv ./payment-sv
if podman container exists payment-sv; then
    echo "El contenedor payment-sv ya existe. Reiniciando..."
    podman restart payment-sv
else
    podman run -d \
      --name payment-sv \
      --memory 768m \
      --restart=always \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -e DB_URL=hackacode-db:3306 \
      -e DB_NAME=payment_db \
      -e EUREKA_URL=eureka-sv:8761 \
      -e KAFKA_URL="${KAFKA_URL}" \
      -e KAFKA_PASSWORD="${KAFKA_PASSWORD}" \
      -e KAFKA_TRUSTSTORE_LOCATION="/app/certs/truststore.jks" \
      -e KAFKA_TRUSTSTORE_PASSWORD="${KAFKA_TRUSTSTORE_PASSWORD}" \
      -e KAFKA_USER="${KAFKA_USER}" \
      -e PROFILE=prod \
      -p 8085:8085 \
      -v "${PAYMENT_DIR}/payment-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      -v "${KAFKA_TRUSTSTORE_LOCATION}:/app/certs/truststore.jks:ro" \
      --network hackacode \
      payment-sv
fi

wait_for patient-sv "wget -qO- http://localhost:8082/actuator/health"
wait_for consultation-sv "wget -qO- http://localhost:8086/actuator/health"
wait_for payment-sv "wget -qO- http://localhost:8085/actuator/health"

##############################
# 5. Servicio Person (depende de varios anteriores)
##############################

echo "Compilando e iniciando person-sv..."
podman build -t person-sv ./person-sv
if podman container exists person-sv; then
    echo "El contenedor person-sv ya existe. Reiniciando..."
    podman restart person-sv
else
    podman run -d \
      --name person-sv \
      --memory 768m \
      --restart=always \
      -e DB_USER=root \
      -e DB_PASSWORD=root \
      -e DB_URL=hackacode-db:3306 \
      -e DB_NAME=person_db \
      -e EUREKA_URL=eureka-sv:8761 \
      -e PROFILE=prod \
      -p 8084:8084 \
      -v "${PERSON_DIR}/person-sv-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      --network hackacode \
      person-sv

    sleep 30
fi
wait_for person-sv "wget -qO- http://localhost:8084/actuator/health"

##############################
# 6. Servicio Apollo Federation (depende de person-sv)
##############################

echo "Compilando e iniciando apollo-federation..."
podman build -t apollo-federation ./apollo-federation-gateway
if podman container exists apollo-federation; then
    echo "El contenedor apollo-federation ya existe. Reiniciando..."
    podman restart apollo-federation
else
    podman run -d \
      --name apollo-federation \
      --memory 256m \
      --restart=always \
      -p 4000:4000 \
      --network hackacode \
      apollo-federation
fi
sleep 10

##############################
# 7. Servicio API Gateway (depende de apollo-federation)
##############################

echo "Compilando e iniciando api-gateway..."
podman build -t api-gateway ./api-gateway
if podman container exists api-gateway; then
    echo "El contenedor api-gateway ya existe. Reiniciando..."
    podman restart api-gateway
else
    podman run -d \
      --name api-gateway \
      --memory 512m \
      --restart=always \
      -e ISSUER_URI="${ISSUER_URI}" \
      -e EUREKA_URL=eureka-sv:8761 \
      -p 443:443 \
      -v "${GATEWAY_DIR}/api-gateway-0.0.1-SNAPSHOT.jar:/app/app.jar" \
      --network hackacode \
      api-gateway
fi

echo "Todos los servicios han sido lanzados."
