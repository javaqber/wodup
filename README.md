WodUp 🏋️‍♂️ - Sistema de Gestión para Boxes de CrossFit

📋 Descripción

WodUp es una aplicación web Full-Stack diseñada para la gestión integral de centros de entrenamiento funcional y CrossFit. Este proyecto (desarrollado como TFG) soluciona la problemática de la dispersión de herramientas en la gestión deportiva, centralizando reservas, aforos y usuarios en una única plataforma SaaS.

La aplicación permite a los Coaches planificar clases y gestionar aforos, mientras que los Atletas pueden reservar su plaza en tiempo real a través de una interfaz moderna y reactiva.

🚀 Funcionalidades Principales

🔒 Seguridad y Autenticación

Login y Registro seguros mediante JWT (JSON Web Tokens).

Encriptación de contraseñas con BCrypt.

Control de acceso basado en Roles (RBAC): ROLE_ADMIN, ROLE_COACH, ROLE_ATHLETE.

📅 Gestión de Clases (Coach)

Creación de clases con control de aforo máximo, horarios y fechas.

Validación de lógica de negocio (fechas pasadas, horas incoherentes).

🙋‍♂️ Experiencia del Atleta

Visualización de Clases: Panel principal con las clases disponibles filtradas en tiempo real (las clases pasadas desaparecen automáticamente).

Reservas: Sistema de reserva de plaza con validación de cupo y duplicidad.

Gestión de Reservas: Posibilidad de cancelar reservas y ver el estado ("Mis Reservas").

🛠️ Stack Tecnológico

Backend (API REST)

Lenguaje: Java 21 (JDK 21)

Framework: Spring Boot 3

Seguridad: Spring Security 6 + JWT

Persistencia: Spring Data JPA (Hibernate)

Base de Datos: MySQL 8

Frontend (SPA)

Framework: Angular 17+ (Standalone Components)

Lenguaje: TypeScript

Estilos: Tailwind CSS

Estado: Angular Signals y RxJS

Comunicación: HTTP Client con Interceptores para JWT

⚙️ Instalación y Despliegue Local

Sigue estos pasos para ejecutar el proyecto en tu máquina local.

Prerrequisitos

Java JDK 21

Node.js (v18 o superior) y npm

MySQL Server

1. Configuración de Base de Datos

Crea una base de datos vacía en MySQL llamada wodup.

CREATE DATABASE wodup;

2. Configuración del Backend

Navega a la carpeta del servidor:

cd wodup_backend

Configura tus credenciales de base de datos en src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/wodup
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA

# JWT Secret (asegúrate de cambiar esto en producción)

wodup.jwt.secret=TU_CLAVE_SECRETA_BASE64...

Ejecuta la aplicación:

mvn spring-boot:run

El servidor iniciará en http://localhost:8080.

3. Configuración del Frontend

Navega a la carpeta del cliente:

cd wodup-frontend

Instala las dependencias:

npm install

Inicia el servidor de desarrollo:

ng serve

La aplicación estará disponible en http://localhost:4200.

📸 Capturas de Pantalla

(Aquí puedes añadir imágenes de tu aplicación. Guarda las capturas en una carpeta /assets en tu repo y enlázalas aquí)

Login

Dashboard Reservas

🧪 Testing de la API (Postman)

El proyecto incluye una colección de endpoints para probar con Postman:

POST /api/auth/login: Obtener Token.

POST /api/clases: Crear clase (Requiere token Coach).

POST /api/clases/reservar/{id}: Reservar (Requiere token Athlete).

👤 Autor

[Tu Nombre Completo]

Desarrollador Full-Stack (Java/Angular)

[LinkedIn](Enlace a tu LinkedIn)

[Portfolio](Enlace a tu web si tienes)

Desarrollado como Proyecto de Fin de Grado (TFG) - Desarrollo de Aplicaciones Web (DAW).
