package com.stronck.appmovil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private DatabaseHelper database;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = new DatabaseHelper(this);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        Button register = findViewById(R.id.registerButton);
        Button back = findViewById(R.id.backButton);
        register.setOnClickListener(view -> register());
        back.setOnClickListener(view -> finish());
    }

    private void register() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmation = confirmPasswordInput.getText().toString();
        if (!AuthValidator.isValidName(name)) {
            nameInput.setError("Ingresa un nombre de hasta 80 caracteres."); return;
        }
        if (!AuthValidator.isValidEmail(email)) {
            emailInput.setError("Ingresa un correo válido."); return;
        }
        if (!AuthValidator.isValidPassword(password)) {
            passwordInput.setError("Usa entre 8 y 128 caracteres."); return;
        }
        if (!AuthValidator.passwordsMatch(password, confirmation)) {
            confirmPasswordInput.setError("Las contraseñas no coinciden."); return;
        }
        if (!database.registerUser(name, email, password)) {
            Toast.makeText(this, "No se pudo registrar. Es posible que el correo ya exista.",
                    Toast.LENGTH_LONG).show(); return;
        }
        Toast.makeText(this, "Cuenta creada. Ya puedes iniciar sesión.",
                Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override protected void onDestroy() {
        if (database != null) database.close();
        super.onDestroy();
    }
}
