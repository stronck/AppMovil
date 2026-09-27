package com.stronck.appmovil;

import org.junit.Test;
import static org.junit.Assert.*;

public class AuthValidatorTest {
    @Test public void acceptsValidName() { assertTrue(AuthValidator.isValidName("Ana Pérez")); }
    @Test public void rejectsBlankName() { assertFalse(AuthValidator.isValidName("   ")); }
    @Test public void acceptsValidEmail() { assertTrue(AuthValidator.isValidEmail("ana@example.com")); }
    @Test public void rejectsInvalidEmail() { assertFalse(AuthValidator.isValidEmail("ana@")); }
    @Test public void enforcesPasswordLength() {
        assertTrue(AuthValidator.isValidPassword("Clave123"));
        assertFalse(AuthValidator.isValidPassword("123"));
    }
    @Test public void comparesPasswordConfirmation() {
        assertTrue(AuthValidator.passwordsMatch("Clave123", "Clave123"));
        assertFalse(AuthValidator.passwordsMatch("Clave123", "otraClave"));
    }
}
