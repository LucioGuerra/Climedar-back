#!/bin/bash


set -a
source .env-build-all
set -a


SERVICIOS=("api-gateway" "consultation-sv" "doctor-sv" "eureka-sv" "medical-service-sv" "notification-sv" "patient-sv" "payme                       nt-sv" "person-sv")

for servicio in "${SERVICIOS[@]}"; do
    echo "Procesando $servicio..."
    cd $servicio || { echo "No se pudo acceder a /$servicio"; continue; }

    if [ "$servicio" == "consultation-sv" ]; then
        export DB_NAME="consultation_db"
    elif [ "$servicio" == "doctor_sv" ]; then
        export DB_NAME="doctor_db"
    elif [ "$servicio" == "medical-service-sv" ]; then
        export DB_NAME="medical_service_db"
    elif [ "$servicio" == "patient-sv" ]; then
        export DB_NAME="patient_db"
    elif [ "$servicio" == "person-sv" ]; then
        export DB_NAME="person_db"
    elif [ "$servicio" == "payment_db" ]; then
        export DB_NAME="payment_db"
    fi

    if [ "$servicio" == "payment-sv" ]; then
        mvn clean package -DskipTests
    else
        mvn clean package
    fi

    if [ $? -ne 0 ]; then
        echo "Error al construir $servicio"
    else
        echo "Construcción exitosa de $servicio"
    fi

    # Regresa al directorio base
    cd .. || exit
done

echo "Proceso completado."
