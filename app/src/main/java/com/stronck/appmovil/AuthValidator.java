package com.stronck.appmovil;

import java.util.regex.Pattern;

public final class AuthValidator {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private AuthValidator() { }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() <= 80;
    }
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.length() <= 128;
    }
    public static boolean passwordsMatch(String password, String confirmation) {
        return password != null && password.equals(confirmation);
    }
}
