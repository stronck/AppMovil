package com.stronck.appmovil;

/** Modelo inmutable de una tarea. */
public final class Task {
    private final String title;
    private boolean completed;

    public Task(String title) {
        if (!TaskValidator.isValidTitle(title)) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.title = title.trim();
        this.completed = false;
    }

    public String getTitle() { return title; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
