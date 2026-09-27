package com.stronck.appmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private DatabaseHelper database;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        if (prefs.getBoolean("logged_in", false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        database = new DatabaseHelper(this);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button login = findViewById(R.id.loginButton);
        Button register = findViewById(R.id.goRegisterButton);
        login.setOnClickListener(view -> login());
        register.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        if (!AuthValidator.isValidEmail(email) || password.isEmpty()) {
            Toast.makeText(this, "Ingresa un correo válido y tu contraseña.", Toast.LENGTH_LONG).show();
            return;
        }
        DatabaseHelper.User user = database.authenticate(email, password);
        if (user == null) {
            Toast.makeText(this, "Credenciales incorrectas.", Toast.LENGTH_LONG).show();
            return;
        }
        getSharedPreferences("session", MODE_PRIVATE).edit()
                .putBoolean("logged_in", true).putLong("user_id", user.id)
                .putString("user_name", user.name).apply();
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override protected void onDestroy() {
        if (database != null) database.close();
        super.onDestroy();
    }
}
