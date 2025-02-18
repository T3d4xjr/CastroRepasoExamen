package com.example.repaso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtUsuario, txtRol;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtRol = findViewById(R.id.txtRol);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String usuario = intent.getStringExtra("usuario");
        String contraseña = intent.getStringExtra("contraseña");

        String rol = dbHelper.obtenerRol(usuario, contraseña);

        txtUsuario.setText("Usuario: " + usuario);
        txtRol.setText("Rol: " + rol);

        if (usuario.equals("admin") && contraseña.equals("1234")) {
            txtRol.setText("Rol: Administrador");

            Intent adminIntent = new Intent(PerfilActivity.this, AdminActivity.class);
            startActivity(adminIntent);
            finish();

        } else if (rol.equals("Cliente")) {
            Intent clienteIntent = new Intent(PerfilActivity.this, ClienteActivity.class);
            startActivity(clienteIntent);
            finish();

        } else {
            Toast.makeText(this, "Rol no válido", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(PerfilActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
