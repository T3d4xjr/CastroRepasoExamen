package com.example.repaso;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClienteActivity extends AppCompatActivity {

    private ListView listaProductos;
    private Spinner spinnerRecreo;
    private Button btnSeleccionarFecha, btnSeleccionarHora, btnHacerPedido;
    private TextView txtFechaSeleccionada, txtHoraSeleccionada;
    private String fechaSeleccionada, horaSeleccionada;
    private List<Product> productList;
    private Product productoSeleccionado;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu Cliente");

        listaProductos = findViewById(R.id.listaProductos);
        spinnerRecreo = findViewById(R.id.spinnerRecreo);
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        btnHacerPedido = findViewById(R.id.btnHacerPedido);
        txtFechaSeleccionada = findViewById(R.id.txtFechaSeleccionada);
        txtHoraSeleccionada = findViewById(R.id.txtHoraSeleccionada);

        dbHelper = new DatabaseHelper(this);

        configurarSpinner();
        configurarListaProductos();

        btnSeleccionarFecha.setOnClickListener(v -> seleccionarFecha());
        btnSeleccionarHora.setOnClickListener(v -> seleccionarHora());
        btnHacerPedido.setOnClickListener(v -> realizarPedido());
    }

    private void configurarSpinner() {
        List<String> recreos = new ArrayList<>();
        recreos.add("Recreo 1");
        recreos.add("Recreo 2");
        recreos.add("Recreo 3");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recreos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecreo.setAdapter(adapter);
    }

    private void configurarListaProductos() {
        productList = new ArrayList<>();
        productList.add(new Product("Producto 1", 15.99, R.drawable.ic_launcher_background));
        productList.add(new Product("Producto 2", 12.50, R.drawable.ic_launcher_foreground));
        productList.add(new Product("Producto 3", 8.99, R.drawable.baseline_login_24));

        CustomAdapter adapter = new CustomAdapter(this, productList);
        listaProductos.setAdapter(adapter);

        listaProductos.setOnItemClickListener((parent, view, position, id) -> {
            productoSeleccionado = productList.get(position);
            Toast.makeText(ClienteActivity.this, "Producto seleccionado: " + productoSeleccionado.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void seleccionarFecha() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth1) -> {
                    fechaSeleccionada = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                    txtFechaSeleccionada.setText("Fecha seleccionada: " + fechaSeleccionada);
                },
                year,
                month,
                dayOfMonth
        );
        datePickerDialog.show();
    }

    private void seleccionarHora() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute1);
                    txtHoraSeleccionada.setText("Hora seleccionada: " + horaSeleccionada);
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void realizarPedido() {
        if (fechaSeleccionada == null || horaSeleccionada == null) {
            Toast.makeText(this, "Por favor, selecciona fecha y hora", Toast.LENGTH_SHORT).show();
        } else if (productoSeleccionado == null) {
            Toast.makeText(this, "Por favor, selecciona un producto", Toast.LENGTH_SHORT).show();
        } else {
            String recreoSeleccionado = spinnerRecreo.getSelectedItem().toString();
            double precio = productoSeleccionado.getPrice();
            String productoNombre = productoSeleccionado.getName();

            boolean exito = dbHelper.insertPedido(recreoSeleccionado, fechaSeleccionada, horaSeleccionada, productoNombre, precio, "pendiente");

            if (exito) {
                Toast.makeText(this, "Pedido realizado correctamente", Toast.LENGTH_SHORT).show();
                limpiarFormulario();
            } else {
                Toast.makeText(this, "Error al realizar el pedido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void limpiarFormulario() {
        fechaSeleccionada = null;
        horaSeleccionada = null;
        productoSeleccionado = null;
        txtFechaSeleccionada.setText("Fecha seleccionada:");
        txtHoraSeleccionada.setText("Hora seleccionada:");
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
                Intent intentPerfil = new Intent(this, PerfilActivity.class);
                startActivity(intentPerfil);
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
}
