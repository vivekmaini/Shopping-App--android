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
        Button orderBtn = findViewById(R.id.orderBtn);
        dbHelper = new DBHelper(this);

        // open cart
        btnCart.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Activity_Cart.class));
        });
        orderBtn.setOnClickListener(v -> {

            startActivity(new Intent(this, OrderHistoryActivity.class));

        });

        //  insert products only once
        if (dbHelper.isProductTableEmpty()) {
            dbHelper.insertProduct("Shoes", 999, R.drawable.shoes);
            dbHelper.insertProduct("T-Shirt", 499, R.drawable.shirts);
            dbHelper.insertProduct("Watch", 1999, R.drawable.watch);
            dbHelper.insertProduct("Headphones", 1499, R.drawable.headphones);
            dbHelper.insertProduct("Smartphone", 15999, R.drawable.mobile);
            dbHelper.insertProduct("Laptop", 55999, R.drawable.laptop);
            dbHelper.insertProduct("Backpack", 799, R.drawable.bag);
            dbHelper.insertProduct("Sunglasses", 699, R.drawable.glasses);
            dbHelper.insertProduct("Sneakers", 1299, R.drawable.sneakers);
            dbHelper.insertProduct("Perfume", 999, R.drawable.perfume);
        }


        List<Product> list = dbHelper.getProducts();


        ProductAdapter adapter = new ProductAdapter(this, list);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}