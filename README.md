WodUp 🏋️‍♂️ - Sistema de Gestión para Boxes de CrossFit

## 📋 Descripción

WodUp es una aplicación web Full-Stack diseñada para la gestión integral de centros de entrenamiento funcional y CrossFit. Este proyecto (desarrollado como TFG) soluciona la problemática de la dispersión de herramientas en la gestión deportiva, centralizando reservas, aforos y usuarios en una única plataforma SaaS.

La aplicación permite a los Coaches planificar clases y gestionar aforos, mientras que los Atletas pueden reservar su plaza en tiempo real a través de una interfaz moderna y reactiva.

## 📍 Demo en Vivo
Puedes probar la aplicación aquí: 
[https://wodup.netlify.app](https://wodup.netlify.app)
**Usuario de prueba:** manuelvega@wodup.com | **Password:** 123456

## 🛠️ Stack Tecnológico
# Backend (API REST)
** Java 21 (JDK 21) **
** Spring Boot 3 **
** Spring Security 6 + JWT **
** Spring Data JPA (Hibernate) **
** MySQL 8 **

# Frontend (SPA)
** Angular 17+ (Standalone Components) **
** TypeScript **
** Tailwind CSS **
** Angular Signals y RxJS **

# Infraestructua y Devops
** Render ** (Despliegue de API)
** Netlify ** (Despliegue de Frontend)
** Aiven ** (Migración a DB remota)

## 🚀 Funcionalidades Principales

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


## 🧪 Testing de la API (Postman)

El proyecto incluye una colección de endpoints para probar con Postman:

POST /api/auth/login: Obtener Token.

POST /api/clases: Crear clase (Requiere token Coach).

POST /api/clases/reservar/{id}: Reservar (Requiere token Athlete).

## 👤 Autor

- Javier Vaquero Berrocal

Desarrollador Full-Stack (Java/Angular)

[LinkedIn] www.linkedin.com/in/javier-vaquero-dev35b5176

[Portfolio] ###

Desarrollado como Proyecto de Fin de Grado (TFG) - Desarrollo de Aplicaciones Web (DAW).
