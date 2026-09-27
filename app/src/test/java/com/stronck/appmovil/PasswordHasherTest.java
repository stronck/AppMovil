package com.stronck.appmovil;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordHasherTest {
    @Test public void verifiesCorrectPassword() {
        String salt = PasswordHasher.newSalt();
        String hash = PasswordHasher.hash("ClaveSegura123", salt);
        assertTrue(PasswordHasher.verify("ClaveSegura123", salt, hash));
        assertFalse(PasswordHasher.verify("OtraClave123", salt, hash));
    }
    @Test public void createsDifferentSalts() {
        assertNotEquals(PasswordHasher.newSalt(), PasswordHasher.newSalt());
    }
}
