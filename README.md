# Gestión de tareas web — GA8-220501096-AA1-EV01

Aplicación **orientada a la web** para gestionar tareas, desarrollada con HTML, CSS y JavaScript en el cliente, Python/Flask en el servidor y SQLite para persistencia. No utiliza Firebase.

La implementación web de la evidencia está en **`web/`**. La carpeta `app/` conserva el código Android que ya existía en el repositorio; para ejecutar y evaluar la versión web, sigue las instrucciones de `web/README.md`.

## Funcionalidades web
- Crear tareas con título obligatorio.
- Consultar las tareas guardadas.
- Marcar y desmarcar tareas como completadas.
- Eliminar tareas.
- Conservar los datos en SQLite después de reiniciar el servidor.

## Requisitos
- Python 3.10 o superior.
- pip.

## Ejecutar la versión web
Desde la raíz del repositorio:

```bash
python -m venv .venv
source .venv/bin/activate
python -m pip install -r web/requirements.txt
cd web
python app.py
```

En Windows, activa el entorno con `.venv\\Scripts\\activate`. Abre http://127.0.0.1:5000 en el navegador.

## Ejecutar pruebas web
Desde la carpeta `web`:

```bash
python -m unittest discover -s tests -v
```

Las pruebas están definidas en `web/tests/test_app.py`. Ejecutarlas en un entorno configurado y guardar el resultado para documentar la verificación.

## Arquitectura web
- **Presentación:** HTML5, CSS3 y JavaScript en `web/templates/` y `web/static/`.
- **Aplicación/API:** rutas Flask en `web/app.py`.
- **Persistencia:** SQLite, con consultas parametrizadas.
- **Pruebas:** `unittest` y el cliente de pruebas de Flask.

La aplicación escucha por defecto en `127.0.0.1` y no incluye autenticación multiusuario. No debe exponerse a Internet sin añadir controles de acceso y configuración de despliegue apropiados.

Consulta `web/README.md` para instalación, dependencias, seguridad, arquitectura y pruebas.
