package com.stronck.appmovil;

/** Modelo de una tarea almacenada localmente. */
public final class Task {
    private final long id;
    private final String title;
    private boolean completed;

    /** Crea una tarea nueva que todavía no tiene identificador en la base de datos. */
    public Task(String title) {
        this(-1L, title, false);
    }

    public Task(long id, String title, boolean completed) {
        if (!TaskValidator.isValidTitle(title)) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.id = id;
        this.title = title.trim();
        this.completed = completed;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
