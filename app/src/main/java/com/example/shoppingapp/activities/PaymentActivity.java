package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.database.DBHelper;

public class PaymentActivity extends AppCompatActivity {

    RadioGroup paymentOptions;
    Button payBtn;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentOptions = findViewById(R.id.paymentOptions);
        payBtn = findViewById(R.id.payBtn);

        dbHelper = new DBHelper(this);

        payBtn.setOnClickListener(v -> {

            int selectedId = paymentOptions.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Select payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.clearCart();  // 🧹 cart clear

            Toast.makeText(this, "Payment Successful 🎉", Toast.LENGTH_LONG).show();

            finish(); // वापस cart पे
        });
    }
}