package com.example.shoppingapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.database.DBHelper;
import com.example.shoppingapp.model.Product;
import com.example.shoppingapp.adapter.InvoiceAdapter;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView recyclerView = findViewById(R.id.orderRecycler);

        DBHelper dbHelper = new DBHelper(this);
        List<Product> list = dbHelper.getOrders();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new InvoiceAdapter(this, list));
    }
}