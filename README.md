<<<<<<< HEAD
# Finnancy Spring API

API REST desarrollada con Spring Boot para la gestión financiera personal.

## Características

- **Autenticación**: Integración con Firebase Authentication
- **Gestión de Transacciones**: CRUD completo para ingresos y gastos
- **Metas de Ahorro**: Sistema de seguimiento de objetivos financieros
- **Reportes**: Generación de reportes financieros personalizados
- **IA Financiera**: Integración con OpenAI para consejos financieros
- **Notificaciones**: Sistema de correo electrónico
- **Documentación**: API documentada con Swagger/OpenAPI

## Tecnologías

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Security**
- **Spring Data MongoDB**
- **Firebase Admin SDK**
- **OpenAI API**
- **Spring Mail**
- **Swagger/OpenAPI 3**

## Configuración

### Prerrequisitos

- Java 21 o superior
- MongoDB
- Cuenta de Firebase
- API Key de OpenAI
- Cuenta de Gmail para envío de correos

### Variables de Entorno

Configura las siguientes variables en `application.properties`:

```properties
# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=finnancy
spring.data.mongodb.username=tu_usuario
spring.data.mongodb.password=tu_password

# OpenAI
openai.api.key=tu_api_key_openai

# Gmail
spring.mail.username=tu_email@gmail.com
GMAIL_PASSWORD=tu_password_app
```

### Firebase

1. Descarga el archivo `firebase-service-account.json` desde Firebase Console
2. Colócalo en `src/main/resources/`

## Instalación y Ejecución

```bash
# Clonar el repositorio
git clone <repository-url>
cd finnancy-spring-api

# Compilar y ejecutar
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Documentación API

Una vez ejecutada la aplicación, la documentación Swagger estará disponible en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Endpoints Principales

### Autenticación
- `POST /auth/register` - Registrar usuario
- `POST /auth/login` - Iniciar sesión
- `POST /auth/forgotPassword` - Recuperar contraseña

### Transacciones
- `GET /api/transactions` - Listar transacciones
- `POST /api/transactions` - Crear transacción
- `PUT /api/transactions/{id}` - Actualizar transacción
- `DELETE /api/transactions/{id}` - Eliminar transacción
- `GET /api/transactions/summary/{userId}` - Resumen financiero

### Metas de Ahorro
- `GET /api/savings-goals/user/{userId}` - Listar metas
- `POST /api/savings-goals` - Crear meta
- `PUT /api/savings-goals/{id}` - Actualizar meta
- `POST /api/savings-goals/{id}/add` - Agregar fondos
- `DELETE /api/savings-goals/{id}` - Eliminar meta

### IA Financiera
- `POST /api/ai/ask/{userId}` - Consultar IA financiera

### Reportes
- `GET /api/reports` - Generar reportes

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/finnancy/back/
│   │   ├── config/          # Configuraciones
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── enums/          # Enumeraciones
│   │   ├── model/          # Entidades/Modelos
│   │   ├── repository/     # Repositorios MongoDB
│   │   └── service/        # Lógica de negocio
│   └── resources/
│       ├── application.properties
│       └── firebase-service-account.json
└── test/                   # Pruebas unitarias
```

## Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT.
=======
# finnancy-Backend-Definitive
>>>>>>> 475f8ea4cb8ce7ce9997c8b750f31445c1ffbed2
