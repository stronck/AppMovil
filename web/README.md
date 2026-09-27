# Aplicación web de gestión de tareas

Versión web de la evidencia **GA8-220501096-AA1-EV01**. Interfaz HTML/CSS/JavaScript, servidor Python con Flask y persistencia SQLite. No utiliza Firebase.

## Requisitos
- Python 3.10 o superior.
- pip.

## Instalación y ejecución
Desde la raíz del repositorio:

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -r web/requirements.txt
cd web
python app.py
```

En Windows, activa el entorno con `.venv\\Scripts\\activate`. Abre http://127.0.0.1:5000 en el navegador. La aplicación escucha por defecto solo en la interfaz local; no está configurada para exposición pública.

La base de datos se crea automáticamente en `web/instance/tasks.sqlite3`. Para usar otra ruta, define la variable de entorno `TASKS_DATABASE`.

## Funcionalidades
- Crear tareas con título obligatorio de hasta 200 caracteres.
- Consultar la lista de tareas.
- Marcar y desmarcar tareas como completadas.
- Eliminar tareas.
- Conservar los datos en SQLite después de reiniciar el servidor.

## Arquitectura por capas
- **Presentación:** `templates/index.html`, `static/styles.css` y `static/app.js`.
- **Aplicación/API:** rutas Flask `/api/tasks` para listar, crear, actualizar y eliminar.
- **Persistencia:** SQLite, con consultas parametrizadas.
- **Pruebas:** `tests/test_app.py`, usando el cliente de pruebas de Flask y una base temporal.

## Tecnologías y dependencias
- Python: lenguaje del servidor.
- Flask: framework web y enrutamiento HTTP.
- SQLite: base de datos relacional integrada en Python.
- HTML5, CSS3 y JavaScript: interfaz web, sin framework JavaScript externo.
- unittest: biblioteca de pruebas incluida en Python.
- pip y venv: instalación y aislamiento de dependencias.

## Seguridad implementada y límites
- Validación del tipo, longitud y contenido del título tanto en el cliente como en el servidor.
- Consultas SQL parametrizadas.
- Los títulos se insertan en el DOM mediante `textContent`, no como HTML.
- Cabeceras CSP, X-Content-Type-Options, X-Frame-Options y Referrer-Policy.
- El servidor usa `debug=False` y escucha en `127.0.0.1` por defecto.
- Esta versión no incluye autenticación ni autorización multiusuario. No debe publicarse en Internet sin añadir control de acceso, configuración de despliegue y revisión de seguridad.

## Pruebas
Desde la carpeta `web`:

```bash
python -m pip install -r requirements.txt
python -m unittest discover -s tests -v
```

Las pruebas cubren página inicial, listado, creación y persistencia, validaciones, actualización, eliminación, respuestas 404 y cabeceras de seguridad. Registrar el resultado real de la ejecución antes de presentar la evidencia.

## Control de versiones
Los archivos de esta versión web se encuentran en la carpeta `web/` del repositorio. Los cambios se registran mediante commits de Git.
