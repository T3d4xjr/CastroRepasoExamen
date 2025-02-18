package com.example.repaso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> productList;

    public CustomAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        this.context = context;
        this.productList = productList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        Product product = productList.get(position);

        ImageView imgProducto = convertView.findViewById(R.id.imgProducto);
        TextView txtProducto = convertView.findViewById(R.id.txtProducto);
        TextView txtPrecio = convertView.findViewById(R.id.txtPrecio);

        // Set image, product name, and price
        imgProducto.setImageResource(product.getImageResourceId());
        txtProducto.setText(product.getName());
        txtPrecio.setText("Precio: $" + product.getPrice());

        return convertView;
    }
}
