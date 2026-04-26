package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.database.DBHelper;
import com.example.shoppingapp.adapter.CartAdapter;
import com.example.shoppingapp.model.Product;
import com.example.shoppingapp.R;
import android.content.Intent;

import java.util.List;

public class Activity_Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView totalPrice;
    Button checkoutBtn;

    DBHelper dbHelper;
    CartAdapter adapter;
    List<Product> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // ✅ पहले findViewById
        recyclerView = findViewById(R.id.cartRecycler);
        totalPrice = findViewById(R.id.totalPrice);
        checkoutBtn = findViewById(R.id.checkoutBtn);

        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCart();

        // ✅ अब listener लगाओ
        checkoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Cart.this, AddressActivity.class);
            startActivity(intent);
        });
    }

    private void loadCart() {
        list = dbHelper.getCartItems();

        adapter = new CartAdapter(this, list);
        recyclerView.setAdapter(adapter);

        double total = dbHelper.getTotalPrice();
        totalPrice.setText("Total: ₹ " + total);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

}