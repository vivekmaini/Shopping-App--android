package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import com.example.shoppingapp.activities.InvoiceActivity;
import com.example.shoppingapp.R;
import com.example.shoppingapp.database.DBHelper;
import com.example.shoppingapp.model.Product;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    RadioGroup paymentOptions;
    Button payBtn;
    DBHelper dbHelper;
    double finalAmount;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentOptions = findViewById(R.id.paymentOptions);
        payBtn = findViewById(R.id.payBtn);

        dbHelper = new DBHelper(this);

        //  GET DATA
        finalAmount = getIntent().getDoubleExtra("total", 0);
        address = getIntent().getStringExtra("address");

        if (finalAmount == 0) {
            finalAmount = dbHelper.getTotalPrice();
        }

        payBtn.setOnClickListener(v -> {

            int selectedId = paymentOptions.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
                return;
            }


            List<Product> list = dbHelper.getCartItems();


            String date = new SimpleDateFormat("dd MMM yyyy",
                    Locale.getDefault()).format(new Date());
            for (Product p : list) {

                dbHelper.insertOrder(
                        p.getName(),
                        p.getPrice(),
                        p.getQuantity(),
                        p.getImage(),

                        date

                );

            }
            if (list == null || list.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }


            //  OPEN INVOICE
            Intent intent = new Intent(PaymentActivity.this, InvoiceActivity.class);
            intent.putExtra("total", finalAmount);
            intent.putExtra("address", address);

            startActivity(intent);

            //  CLEAR CART

            Toast.makeText(this, "Payment Successful 🎉", Toast.LENGTH_SHORT).show();

            finish();
        });

    }
}