package com.stronck.appmovil;

import org.junit.Test;
import static org.junit.Assert.*;

public class TaskValidatorTest {
    @Test
    public void nullTitleIsInvalid() {
        assertFalse(TaskValidator.isValidTitle(null));
    }

    @Test
    public void emptyTitleIsInvalid() {
        assertFalse(TaskValidator.isValidTitle(""));
    }

    @Test
    public void whitespaceOnlyTitleIsInvalid() {
        assertFalse(TaskValidator.isValidTitle("   \t\n"));
    }

    @Test
    public void nonEmptyTitleIsValid() {
        assertTrue(TaskValidator.isValidTitle("Comprar leche"));
    }

    @Test
    public void taskTrimsTitleWhitespace() {
        Task task = new Task("  Estudiar Java  ");
        assertEquals("Estudiar Java", task.getTitle());
    }

    @Test(expected = IllegalArgumentException.class)
    public void taskRejectsBlankTitle() {
        new Task("   ");
    }

    @Test
    public void taskCanBeMarkedCompleted() {
        Task task = new Task("Entregar evidencia");
        assertFalse(task.isCompleted());
        task.setCompleted(true);
        assertTrue(task.isCompleted());
    }
}
