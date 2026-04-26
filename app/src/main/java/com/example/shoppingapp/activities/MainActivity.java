package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.ProductAdapter;
import com.example.shoppingapp.database.DBHelper;
import com.example.shoppingapp.model.Product;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCart = findViewById(R.id.btnCart);
        recyclerView = findViewById(R.id.recyclerView);

        dbHelper = new DBHelper(this);

        // 👉 open cart
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Activity_Cart.class));
        });

        // 👉 insert products only once
        if (dbHelper.isProductTableEmpty()) {
            dbHelper.insertProduct("Shoes", 999, R.drawable.shoes);
            dbHelper.insertProduct("T-Shirt", 499, R.drawable.shirts);
            dbHelper.insertProduct("Watch", 1999, R.drawable.watch);
        }

        // 👉 get data
        List<Product> list = dbHelper.getProducts();

        // 👉 adapter
        ProductAdapter adapter = new ProductAdapter(this, list);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}