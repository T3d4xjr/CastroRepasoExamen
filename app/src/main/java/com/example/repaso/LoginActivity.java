package com.example.repaso;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsuario, edtContraseña;
    Button btnAcceder, btnCancelar;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = findViewById(R.id.et_username);
        edtContraseña = findViewById(R.id.et_password);
        btnAcceder = findViewById(R.id.btn_login);
        btnCancelar = findViewById(R.id.btn_cancel);

        dbHelper = new DatabaseHelper(this);

        btnAcceder.setOnClickListener(v -> {
            String usuario = edtUsuario.getText().toString().trim();
            String contraseña = edtContraseña.getText().toString().trim();

            if (TextUtils.isEmpty(usuario)) {
                Toast.makeText(LoginActivity.this, "Por favor, ingresa el nombre de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(contraseña)) {
                Toast.makeText(LoginActivity.this, "Por favor, ingresa la contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            if (usuario.equals("admin") && contraseña.equals("1234")) {
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.verificarLogin(usuario, contraseña)) {
                    Intent intent = new Intent(LoginActivity.this, ClienteActivity.class);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Bienvenido Cliente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Usuario no registrado. Registrando nuevo usuario...", Toast.LENGTH_SHORT).show();
                    dbHelper.registrarUsuario(usuario, contraseña);

                    Intent intent = new Intent(LoginActivity.this, ClienteActivity.class);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnCancelar.setOnClickListener(v -> finish());
    }
}
