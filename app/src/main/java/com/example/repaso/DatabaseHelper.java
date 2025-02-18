package com.example.repaso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_PEDIDO_RECREO = "recreo";
    public static final String COLUMN_PEDIDO_FECHA = "fecha";
    public static final String COLUMN_PEDIDO_HORA = "hora";
    public static final String COLUMN_PEDIDO_PRODUCTO = "producto";
    public static final String COLUMN_PEDIDO_PRECIO = "precio";
    public static final String COLUMN_PEDIDO_ESTADO = "estado";
    public static final String COLUMN_ID = "_id";

    private static final String TABLE_USUARIOS = "usuarios";
    private static final String TABLE_PEDIDOS = "pedidos";

    String sqlCreateUsuarios = "CREATE TABLE IF NOT EXISTS " + TABLE_USUARIOS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "usuario TEXT, " +
            "contrasena TEXT" + ")";

    String sqlCreatePedidos = "CREATE TABLE IF NOT EXISTS " + TABLE_PEDIDOS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PEDIDO_RECREO + " TEXT, " +
            COLUMN_PEDIDO_FECHA + " TEXT, " +
            COLUMN_PEDIDO_HORA + " TEXT, " +
            COLUMN_PEDIDO_PRODUCTO + " TEXT, " +
            COLUMN_PEDIDO_PRECIO + " REAL, " +
            COLUMN_PEDIDO_ESTADO + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, "repasoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateUsuarios);
        db.execSQL(sqlCreatePedidos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEDIDOS);
        onCreate(db);
    }

    public boolean verificarLogin(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE usuario =? AND contrasena =?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario, contrasena});

        boolean isValid = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isValid;
    }

    public void registrarUsuario(String usuario, String contrasena) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        db.insert(TABLE_USUARIOS, null, values);
        db.close();
    }

    public boolean insertPedido(String recreo, String fecha, String hora, String producto, double precio, String estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PEDIDO_RECREO, recreo);
        values.put(COLUMN_PEDIDO_FECHA, fecha);
        values.put(COLUMN_PEDIDO_HORA, hora);
        values.put(COLUMN_PEDIDO_PRODUCTO, producto);
        values.put(COLUMN_PEDIDO_PRECIO, precio);
        values.put(COLUMN_PEDIDO_ESTADO, estado);

        long result = db.insert(TABLE_PEDIDOS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor obtenerPedidos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PEDIDOS, null);
    }

    public boolean actualizarEstadoPedido(int id, String estado) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PEDIDO_ESTADO, estado);

        int rowsUpdated = db.update(TABLE_PEDIDOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated > 0;
    }

    public boolean eliminarPedido(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PEDIDOS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    @Override
    public void close() {
        super.close();
    }

    public String obtenerRol(String usuario, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USUARIOS + " WHERE usuario = ? AND contrasena = ?";
        Cursor cursor = db.rawQuery(query, new String[]{usuario, contrasena});

        String rol = "Cliente";
        try {
            if (cursor.moveToFirst()) {
                if (usuario.equals("admin") || contrasena.equals("1234")) {
                    rol = "Administrador";
                }
            }
        } finally {
            cursor.close();
            db.close();
        }
        return rol;
    }
}
