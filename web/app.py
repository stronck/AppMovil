"""Aplicación web de gestión de tareas con Flask y SQLite."""
import os
import sqlite3
from contextlib import closing
from pathlib import Path

from flask import Flask, jsonify, render_template, request


def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)
    app.config.from_mapping(
        DATABASE=os.environ.get(
            "TASKS_DATABASE",
            str(Path(app.instance_path) / "tasks.sqlite3"),
        ),
        MAX_CONTENT_LENGTH=16 * 1024,
    )
    if test_config:
        app.config.update(test_config)

    Path(app.instance_path).mkdir(parents=True, exist_ok=True)

    def connect_db():
        connection = sqlite3.connect(app.config["DATABASE"])
        connection.row_factory = sqlite3.Row
        connection.execute("PRAGMA foreign_keys = ON")
        return connection

    def init_db():
        with closing(connect_db()) as db, db:
            db.execute("""
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL CHECK(length(title) BETWEEN 1 AND 200),
                    completed INTEGER NOT NULL DEFAULT 0 CHECK(completed IN (0, 1)),
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                )
            """)

    init_db()
    app.extensions["connect_tasks_db"] = connect_db

    @app.after_request
    def add_security_headers(response):
        response.headers.setdefault("X-Content-Type-Options", "nosniff")
        response.headers.setdefault("Referrer-Policy", "same-origin")
        response.headers.setdefault("X-Frame-Options", "DENY")
        response.headers.setdefault(
            "Content-Security-Policy",
            "default-src 'self'; script-src 'self'; style-src 'self'; "
            "connect-src 'self'; img-src 'self' data:; object-src 'none'; "
            "base-uri 'self'; frame-ancestors 'none'",
        )
        return response

    def task_to_dict(row):
        return {
            "id": row["id"],
            "title": row["title"],
            "completed": bool(row["completed"]),
            "created_at": row["created_at"],
        }

    @app.get("/")
    def index():
        return render_template("index.html")

    @app.get("/api/tasks")
    def list_tasks():
        with closing(connect_db()) as db, db:
            rows = db.execute(
                "SELECT id, title, completed, created_at FROM tasks "
                "ORDER BY id DESC"
            ).fetchall()
        return jsonify([task_to_dict(row) for row in rows])

    @app.post("/api/tasks")
    def create_task():
        data = request.get_json(silent=True)
        if not isinstance(data, dict):
            return jsonify(error="Se requiere un objeto JSON."), 400
        title = data.get("title")
        if not isinstance(title, str):
            return jsonify(error="El título debe ser texto."), 400
        title = title.strip()
        if not title:
            return jsonify(error="Escribe un título para la tarea."), 400
        if len(title) > 200:
            return jsonify(error="El título no puede superar 200 caracteres."), 400

        with closing(connect_db()) as db, db:
            cursor = db.execute("INSERT INTO tasks (title) VALUES (?)", (title,))
            row = db.execute(
                "SELECT id, title, completed, created_at FROM tasks WHERE id = ?",
                (cursor.lastrowid,),
            ).fetchone()
        return jsonify(task_to_dict(row)), 201

    @app.patch("/api/tasks/<int:task_id>")
    def update_task(task_id):
        data = request.get_json(silent=True)
        if not isinstance(data, dict) or not isinstance(data.get("completed"), bool):
            return jsonify(error="El campo completed debe ser booleano."), 400
        with closing(connect_db()) as db, db:
            cursor = db.execute(
                "UPDATE tasks SET completed = ? WHERE id = ?",
                (int(data["completed"]), task_id),
            )
            if cursor.rowcount == 0:
                return jsonify(error="La tarea no existe."), 404
            row = db.execute(
                "SELECT id, title, completed, created_at FROM tasks WHERE id = ?",
                (task_id,),
            ).fetchone()
        return jsonify(task_to_dict(row))

    @app.delete("/api/tasks/<int:task_id>")
    def delete_task(task_id):
        with closing(connect_db()) as db, db:
            cursor = db.execute("DELETE FROM tasks WHERE id = ?", (task_id,))
        if cursor.rowcount == 0:
            return jsonify(error="La tarea no existe."), 404
        return "", 204

    return app


if __name__ == "__main__":
    application = create_app()
    application.run(host="127.0.0.1", port=int(os.environ.get("PORT", "5000")), debug=False)
