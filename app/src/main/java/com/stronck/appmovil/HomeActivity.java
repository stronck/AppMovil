package com.stronck.appmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        if (!prefs.getBoolean("logged_in", false)) {
            goToLogin(); return;
        }
        setContentView(R.layout.activity_home);
        TextView welcome = findViewById(R.id.welcomeText);
        welcome.setText("¡Hola, " + prefs.getString("user_name", "usuario") + "!");
        Button logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(view -> {
            prefs.edit().clear().apply();
            goToLogin();
        });
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
