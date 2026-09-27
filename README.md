# AppMovil — GA8-220501096-AA1-EV01

Aplicación Android nativa en Java para gestionar una lista sencilla de tareas. Proyecto académico de referencia que demuestra módulos, interfaz XML, navegación/acciones, buenas prácticas y pruebas unitarias.

## Requisitos
- Android Studio compatible con Android Gradle Plugin 8.6.1
- JDK 17
- Android SDK 35

## Ejecutar
1. Clona `https://github.com/stronck/AppMovil.git`.
2. Abre la carpeta en Android Studio y espera la sincronización de Gradle.
3. Ejecuta la configuración `app` en un emulador o dispositivo Android.

## Pruebas unitarias
En Android Studio: clic derecho sobre `app/src/test` > Run, o desde la terminal ejecuta `./gradlew testDebugUnitTest`.

Las pruebas cubren validación de títulos vacíos, espacios en blanco y títulos válidos. Son pruebas unitarias locales; no se afirma que se hayan ejecutado en este entorno.

## Estructura
- `MainActivity`: interacción y presentación de la lista.
- `Task`: modelo de datos de una tarea.
- `TaskValidator`: validación independiente y testeable.
- `res/layout/activity_main.xml`: interfaz XML.
- `app/src/test`: pruebas unitarias JUnit.

## Alcance y seguridad
Esta versión almacena las tareas en memoria: al cerrar el proceso, los datos se reinician. No solicita permisos innecesarios ni contiene credenciales. Firebase no se configura porque requiere un proyecto y un archivo `google-services.json` propios. Para producción se recomienda persistencia local (Room), manejo de errores y pruebas de interfaz.
