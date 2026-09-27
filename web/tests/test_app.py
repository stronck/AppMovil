import os
import tempfile
import unittest

from app import create_app


class TaskWebAppTests(unittest.TestCase):
    def setUp(self):
        self.temp_dir = tempfile.TemporaryDirectory()
        database = os.path.join(self.temp_dir.name, "test.sqlite3")
        self.app = create_app({
            "TESTING": True,
            "DATABASE": database,
        })
        self.client = self.app.test_client()

    def tearDown(self):
        self.temp_dir.cleanup()

    def test_home_page_is_available(self):
        response = self.client.get("/")
        self.assertEqual(response.status_code, 200)
        self.assertIn("Mis tareas".encode(), response.data)

    def test_list_starts_empty(self):
        response = self.client.get("/api/tasks")
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.get_json(), [])

    def test_create_task_trims_title_and_persists(self):
        response = self.client.post("/api/tasks", json={"title": "  Estudiar Java  "})
        self.assertEqual(response.status_code, 201)
        self.assertEqual(response.get_json()["title"], "Estudiar Java")
        listed = self.client.get("/api/tasks").get_json()
        self.assertEqual(len(listed), 1)
        self.assertEqual(listed[0]["title"], "Estudiar Java")

    def test_rejects_blank_title(self):
        for title in ("", "   ", "\t\n"):
            with self.subTest(title=title):
                response = self.client.post("/api/tasks", json={"title": title})
                self.assertEqual(response.status_code, 400)

    def test_rejects_non_string_title(self):
        response = self.client.post("/api/tasks", json={"title": 123})
        self.assertEqual(response.status_code, 400)

    def test_rejects_title_over_200_characters(self):
        response = self.client.post("/api/tasks", json={"title": "x" * 201})
        self.assertEqual(response.status_code, 400)

    def test_update_completion_state(self):
        task = self.client.post("/api/tasks", json={"title": "Probar aplicación"}).get_json()
        response = self.client.patch(
            f"/api/tasks/{task['id']}", json={"completed": True}
        )
        self.assertEqual(response.status_code, 200)
        self.assertTrue(response.get_json()["completed"])

    def test_update_requires_boolean(self):
        task = self.client.post("/api/tasks", json={"title": "Tarea"}).get_json()
        response = self.client.patch(
            f"/api/tasks/{task['id']}", json={"completed": "true"}
        )
        self.assertEqual(response.status_code, 400)

    def test_update_missing_task_returns_404(self):
        response = self.client.patch("/api/tasks/999", json={"completed": True})
        self.assertEqual(response.status_code, 404)

    def test_delete_task(self):
        task = self.client.post("/api/tasks", json={"title": "Eliminar"}).get_json()
        response = self.client.delete(f"/api/tasks/{task['id']}")
        self.assertEqual(response.status_code, 204)
        self.assertEqual(self.client.get("/api/tasks").get_json(), [])

    def test_delete_missing_task_returns_404(self):
        response = self.client.delete("/api/tasks/999")
        self.assertEqual(response.status_code, 404)

    def test_security_headers_are_present(self):
        response = self.client.get("/")
        self.assertEqual(response.headers["X-Frame-Options"], "DENY")
        self.assertIn("default-src 'self'", response.headers["Content-Security-Policy"])


if __name__ == "__main__":
    unittest.main()
