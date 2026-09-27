package com.stronck.appmovil;

/** Reglas de validación separadas de la interfaz para facilitar pruebas unitarias. */
public final class TaskValidator {
    private TaskValidator() { }

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty();
    }
}
