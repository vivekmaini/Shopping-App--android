package com.example.shoppingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.database.DBHelper;
import com.example.shoppingapp.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.shoppingapp.adapter.InvoiceAdapter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        TextView orderId = findViewById(R.id.orderId);
        TextView totalAmount = findViewById(R.id.totalAmount);
        TextView dateTime = findViewById(R.id.dateTime);
        TextView addressText = findViewById(R.id.address);
        RecyclerView recyclerView = findViewById(R.id.invoiceRecycler);
        Button homeBtn = findViewById(R.id.homeBtn);

        //  DATA
        double total = getIntent().getDoubleExtra("total", 0);
        String address = getIntent().getStringExtra("address");

        if (address == null) address = "No Address Available";

        //  List
        DBHelper dbHelper = new DBHelper(this);


        List<Product> list = dbHelper.getCartItems();

        if (list == null) list = new ArrayList<>();

        //  RECYCLER VIEW
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new InvoiceAdapter(this, list));
      // cart clear
        dbHelper.clearCart();
        //  ORDER INFO
        String id = "ORD" + System.currentTimeMillis();
        String date = new SimpleDateFormat("dd MMM yyyy, hh:mm a",
                Locale.getDefault()).format(new Date());

        orderId.setText("Order ID: " + id);
        dateTime.setText("Date: " + date);

        totalAmount.setText("Total Paid: ₹ " + String.format("%.0f", total));
        addressText.setText(address);

        homeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(InvoiceActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

}
