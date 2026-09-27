# AppMovil — GA8-220501096-AA1-EV01

Aplicación Android nativa en Java para gestionar tareas con persistencia local mediante **SQLite**. No utiliza Firebase ni requiere conexión a Internet.

## Requisitos
- Android Studio compatible con Android Gradle Plugin 8.6.1
- JDK 17
- Android SDK 35

## Ejecutar
1. Clona este repositorio.
2. Abre la carpeta en Android Studio y espera la sincronización de Gradle.
3. Ejecuta la configuración `app` en un emulador o dispositivo Android.

## Funcionalidades
- Crear tareas con validación de título.
- Guardar tareas en una base de datos SQLite local.
- Cargar tareas guardadas al abrir nuevamente la aplicación.
- Marcar/desmarcar tareas como completadas.
- Eliminar una tarea manteniéndola pulsada.

## Pruebas unitarias
En Android Studio ejecuta las pruebas de `app/src/test`, o usa:
```bash
./gradlew testDebugUnitTest
```
Las pruebas JUnit cubren títulos nulos/vacíos, espacios en blanco, normalización del título y estado de finalización. Están incluidas en el repositorio; deben ejecutarse en un entorno con JDK/Gradle configurado para confirmar el resultado.

## Arquitectura
- **Presentación:** `MainActivity` y `res/layout/activity_main.xml`.
- **Modelo y validación:** `Task` y `TaskValidator`.
- **Persistencia:** `TaskDatabaseHelper`, basado en `SQLiteOpenHelper`.
- **Recursos:** textos, colores y tema en `res/values`.

## Persistencia y seguridad
La base de datos `appmovil.db` se crea en el almacenamiento privado de la aplicación. No se solicitan permisos de Internet ni se almacenan credenciales. Los datos permanecen después de cerrar y volver a abrir la app; se eliminan al desinstalar la aplicación o borrar sus datos. La migración de esquema debe implementarse cuando cambie la versión de la base de datos.
