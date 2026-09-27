const form = document.querySelector("#task-form");
const titleInput = document.querySelector("#task-title");
const taskList = document.querySelector("#task-list");
const emptyState = document.querySelector("#empty-state");
const taskCount = document.querySelector("#task-count");
const formMessage = document.querySelector("#form-message");
const loadError = document.querySelector("#load-error");

async function api(path, options = {}) {
  const response = await fetch(path, {
    ...options,
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
  });
  if (!response.ok) {
    const payload = await response.json().catch(() => ({}));
    throw new Error(payload.error || "No se pudo completar la operación.");
  }
  return response.status === 204 ? null : response.json();
}

function showMessage(element, message, isError = false) {
  element.textContent = message;
  element.classList.toggle("error", isError);
}

function renderTasks(tasks) {
  taskList.replaceChildren();
  for (const task of tasks) {
    const item = document.createElement("li");
    item.className = "task-item";

    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.completed;
    checkbox.id = `task-${task.id}`;
    checkbox.setAttribute("aria-label", `Marcar como completada: ${task.title}`);
    checkbox.addEventListener("change", async () => {
      checkbox.disabled = true;
      try {
        await api(`/api/tasks/${task.id}`, {
          method: "PATCH",
          body: JSON.stringify({ completed: checkbox.checked }),
        });
        await loadTasks();
      } catch (error) {
        checkbox.checked = !checkbox.checked;
        showMessage(loadError, error.message, true);
      } finally {
        checkbox.disabled = false;
      }
    });

    const label = document.createElement("label");
    label.htmlFor = checkbox.id;
    label.className = task.completed ? "task-title completed" : "task-title";
    // textContent evita interpretar el título introducido como HTML.
    label.textContent = task.title;

    const remove = document.createElement("button");
    remove.type = "button";
    remove.className = "delete-button";
    remove.textContent = "Eliminar";
    remove.setAttribute("aria-label", `Eliminar: ${task.title}`);
    remove.addEventListener("click", async () => {
      remove.disabled = true;
      try {
        await api(`/api/tasks/${task.id}`, { method: "DELETE" });
        await loadTasks();
      } catch (error) {
        showMessage(loadError, error.message, true);
        remove.disabled = false;
      }
    });

    item.append(checkbox, label, remove);
    taskList.append(item);
  }
  const total = tasks.length;
  taskCount.textContent = `${total} ${total === 1 ? "tarea" : "tareas"}`;
  emptyState.hidden = total !== 0;
}

async function loadTasks() {
  try {
    const tasks = await api("/api/tasks");
    renderTasks(tasks);
    loadError.hidden = true;
  } catch (error) {
    showMessage(loadError, error.message, true);
    loadError.hidden = false;
  }
}

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const title = titleInput.value.trim();
  if (!title) {
    showMessage(formMessage, "Escribe un título para la tarea.", true);
    titleInput.focus();
    return;
  }
  if (title.length > 200) {
    showMessage(formMessage, "El título no puede superar 200 caracteres.", true);
    return;
  }

  const submitButton = form.querySelector('button[type="submit"]');
  submitButton.disabled = true;
  try {
    await api("/api/tasks", {
      method: "POST",
      body: JSON.stringify({ title }),
    });
    titleInput.value = "";
    showMessage(formMessage, "Tarea agregada.");
    await loadTasks();
    titleInput.focus();
  } catch (error) {
    showMessage(formMessage, error.message, true);
  } finally {
    submitButton.disabled = false;
  }
});

loadTasks();
