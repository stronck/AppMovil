package com.stronck.appmovil;

import android.util.Base64;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {
    private static final int SALT_BYTES = 16;
    private static final int ITERATIONS = 120000;
    private static final int KEY_LENGTH_BITS = 256;
    private PasswordHasher() { }

    public static String newSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.encodeToString(salt, Base64.NO_WRAP);
    }

    public static String hash(String password, String saltBase64) {
        byte[] salt = Base64.decode(saltBase64, Base64.NO_WRAP);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Base64.encodeToString(factory.generateSecret(spec).getEncoded(), Base64.NO_WRAP);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("No fue posible proteger la contraseña", exception);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verify(String password, String saltBase64, String expectedHash) {
        return hash(password, saltBase64).equals(expectedHash);
    }
}
