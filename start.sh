#!/bin/bash

echo "========================================"
echo "   Finnancy Spring API - Iniciando"
echo "========================================"
echo

echo "Verificando Java..."
if ! command -v java &> /dev/null; then
    echo "ERROR: Java no encontrado. Instala Java 21 o superior."
    exit 1
fi

java -version
echo

echo "Compilando y ejecutando la aplicación..."
echo
./mvnw spring-boot:run
