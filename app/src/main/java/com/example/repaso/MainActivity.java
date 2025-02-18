package com.example.repaso;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pantalla Principal");
        Button btnGoToLogin = findViewById(R.id.btn_login);
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_terms:
                Toast.makeText(this, "Términos y condiciones de nuestra tienda.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_policy:
                Toast.makeText(this, "Política y privacidad de nuestra tienda.", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.item_created_by:
                Toast.makeText(this, "Creado por: Miguel Angel Ruiz Aguilar", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
