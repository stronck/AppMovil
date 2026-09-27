# Arquitectura de AppMovil

## Capas
- Presentación: Activities y layouts XML.
- Aplicación: validación y coordinación de registro e inicio de sesión.
- Persistencia: DatabaseHelper y SQLite.
- Seguridad: PasswordHasher y SharedPreferences para la sesión local.

## Componentes
- MainActivity: pantalla de inicio de sesión.
- RegisterActivity: registro de cuentas.
- HomeActivity: pantalla de sesión iniciada y cierre de sesión.
- AuthValidator: validación de nombre, correo y contraseña.
- PasswordHasher: derivación y verificación de contraseñas mediante PBKDF2-HMAC-SHA256.
- DatabaseHelper: creación de tabla y operaciones de persistencia.

## Esquema SQLite
Tabla users:
- id: INTEGER PRIMARY KEY AUTOINCREMENT.
- name: TEXT NOT NULL.
- email: TEXT NOT NULL COLLATE NOCASE UNIQUE.
- password_hash: TEXT NOT NULL.
- password_salt: TEXT NOT NULL.
- created_at: TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP.

## Navegación
Inicio de sesión -> Registro -> Inicio de sesión -> Inicio (sesión activa) -> Cerrar sesión -> Inicio de sesión.

## Límites
Almacenamiento exclusivamente local; no hay autenticación remota, recuperación de contraseña, verificación de correo ni sincronización entre dispositivos.
