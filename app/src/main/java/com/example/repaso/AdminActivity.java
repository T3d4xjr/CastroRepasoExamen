package com.example.repaso;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity {

    private ListView listaPedidosAdmin;
    private Button btnMarcarComoCompletado, btnEliminar;
    private DatabaseHelper dbHelper;
    private Cursor cursorPedidos;
    private int pedidoSeleccionadoId = -1;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        usuario = getIntent().getStringExtra("usuario");

        listaPedidosAdmin = findViewById(R.id.listaPedidosAdmin);
        btnMarcarComoCompletado = findViewById(R.id.btnMarcarComoCompletado);
        btnEliminar = findViewById(R.id.btnEliminar);
        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu Administrador");

        cargarPedidos();

        listaPedidosAdmin.setOnItemClickListener((parent, view, position, id) -> {
            pedidoSeleccionadoId = (int) id;
            Toast.makeText(AdminActivity.this, "Pedido seleccionado con ID: " + pedidoSeleccionadoId, Toast.LENGTH_SHORT).show();
        });

        btnMarcarComoCompletado.setOnClickListener(v -> {
            if (pedidoSeleccionadoId != -1) {
                boolean result = dbHelper.actualizarEstadoPedido(pedidoSeleccionadoId, "Completado");
                if (result) {
                    Toast.makeText(AdminActivity.this, "Pedido marcado como completado", Toast.LENGTH_SHORT).show();
                    cargarPedidos();
                } else {
                    Toast.makeText(AdminActivity.this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(AdminActivity.this, "Selecciona un pedido primero", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (pedidoSeleccionadoId != -1) {
                new AlertDialog.Builder(AdminActivity.this)
                        .setMessage("¿Seguro que quieres eliminar este pedido?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            boolean result = dbHelper.eliminarPedido(pedidoSeleccionadoId);
                            if (result) {
                                Toast.makeText(AdminActivity.this, "Pedido eliminado", Toast.LENGTH_SHORT).show();
                                cargarPedidos();
                            } else {
                                Toast.makeText(AdminActivity.this, "Error al eliminar el pedido", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(AdminActivity.this, "Selecciona un pedido primero", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.perfil:
                Intent intent = new Intent(AdminActivity.this, PerfilActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                return true;

            case R.id.Cerrarsesion:
                Intent intentLogout = new Intent(this, LoginActivity.class);
                startActivity(intentLogout);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargarPedidos() {
        cursorPedidos = dbHelper.obtenerPedidos();

        if (cursorPedidos != null && cursorPedidos.getCount() > 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    cursorPedidos,
                    new String[]{"producto", "estado"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );

            listaPedidosAdmin.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No hay pedidos disponibles", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursorPedidos != null) {
            cursorPedidos.close();
        }
        dbHelper.close();
    }
}
