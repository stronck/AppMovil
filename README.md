# AppMovil — Registro e inicio de sesión

Aplicación móvil Android nativa en Java con registro, inicio de sesión y almacenamiento local SQLite.

## Funciones
- Registro con nombre, correo y contraseña.
- Inicio y cierre de sesión.
- Validación de campos y correo.
- Contraseñas derivadas con PBKDF2-HMAC-SHA256 y salt aleatorio; no se guardan en texto plano.
- Base de datos SQLite privada del dispositivo.
- Pruebas unitarias de validaciones y hash.

## Ejecutar
Requisitos: Android Studio, JDK 17 y Android SDK 35. Abre la raíz del repositorio en Android Studio, sincroniza Gradle, selecciona un emulador/dispositivo y ejecuta el módulo `app`.

## Pruebas
Ejecuta las pruebas de `app/src/test` desde Android Studio. También puedes usar `gradle test` si tienes Gradle instalado. El proyecto no incluye Gradle Wrapper.

## Alcance y seguridad
La cuenta se almacena localmente; no hay servidor remoto, recuperación de contraseña ni sincronización. Es una implementación académica local, no un sistema de identidad para producción.

Repositorio: https://github.com/stronck/AppMovil
