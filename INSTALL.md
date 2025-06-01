# Guía de Instalación - Finnancy Spring API

## Prerrequisitos

### 1. Java 21
Descarga e instala Java 21 desde:
- [Oracle JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
- [OpenJDK 21](https://openjdk.org/projects/jdk/21/)

Verifica la instalación:
```bash
java -version
```

### 2. MongoDB
Instala MongoDB Community Edition:
- [Guía oficial de instalación](https://docs.mongodb.com/manual/installation/)

#### Configuración básica de MongoDB:
```bash
# Iniciar MongoDB
mongod

# Crear usuario administrador (opcional)
mongosh
use admin
db.createUser({
  user: "root",
  pwd: "123",
  roles: ["userAdminAnyDatabase", "dbAdminAnyDatabase", "readWriteAnyDatabase"]
})
```

### 3. Firebase
1. Crea un proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Habilita Authentication
3. Ve a "Configuración del proyecto" > "Cuentas de servicio"
4. Genera una nueva clave privada
5. Descarga el archivo JSON y renómbralo a `firebase-service-account.json`
6. Colócalo en `src/main/resources/`

### 4. OpenAI API Key
1. Crea una cuenta en [OpenAI](https://platform.openai.com/)
2. Genera una API Key
3. Configúrala en `application.properties`

### 5. Gmail para correos
1. Habilita la verificación en 2 pasos en tu cuenta de Gmail
2. Genera una contraseña de aplicación
3. Configura las variables de entorno

## Instalación

### Paso 1: Clonar el proyecto
```bash
git clone <repository-url>
cd finnancy-spring-api
```

### Paso 2: Configurar variables de entorno
```bash
# Windows
set GMAIL_PASSWORD=tu_password_de_aplicacion

# Linux/Mac
export GMAIL_PASSWORD=tu_password_de_aplicacion
```

### Paso 3: Configurar application.properties
Copia `application-example.properties` a `application-local.properties` y configura:
- MongoDB credentials
- OpenAI API Key
- Gmail credentials

### Paso 4: Ejecutar la aplicación
```bash
# Windows
start.bat

# Linux/Mac
./start.sh

# O manualmente
./mvnw spring-boot:run
```

## Verificación

1. Abre http://localhost:8080/swagger-ui.html
2. Verifica que la documentación API se carga correctamente
3. Prueba el endpoint de test: http://localhost:8080/api/test

## Solución de Problemas

### Error de conexión a MongoDB
- Verifica que MongoDB esté ejecutándose
- Revisa las credenciales en application.properties
- Verifica que el puerto 27017 esté disponible

### Error de Firebase
- Verifica que el archivo firebase-service-account.json esté en resources/
- Revisa que el proyecto de Firebase esté configurado correctamente

### Error de OpenAI
- Verifica que la API Key sea válida
- Revisa que tengas créditos disponibles en tu cuenta de OpenAI

### Error de correo
- Verifica que la contraseña de aplicación de Gmail sea correcta
- Revisa que la verificación en 2 pasos esté habilitada
