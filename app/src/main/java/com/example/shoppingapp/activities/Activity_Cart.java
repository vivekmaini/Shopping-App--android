package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.database.DBHelper;
import com.example.shoppingapp.adapter.CartAdapter;
import com.example.shoppingapp.model.Product;
import com.example.shoppingapp.R;
import android.content.Intent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Activity_Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView totalPrice, discountText, applyCoupon;
    Button checkoutBtn;
    EditText couponInput;

    DBHelper dbHelper;
    CartAdapter adapter;
    List<Product> list;

    double total = 0;
    double discount = 0;

    boolean isCouponApplied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecycler);
        totalPrice = findViewById(R.id.totalPrice);
        discountText = findViewById(R.id.discountText);

        checkoutBtn = findViewById(R.id.checkoutBtn);
        applyCoupon = findViewById(R.id.applyCoupon);
        couponInput = findViewById(R.id.couponInput);

        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCart();



        //  APPLY / REMOVE COUPON
        applyCoupon.setOnClickListener(v -> {

            // REMOVE
            if (isCouponApplied) {

                discount = 0;
                isCouponApplied = false;

                totalPrice.setText("Total: ₹ " + total);
                discountText.setText("Discount: ₹ 0");

                applyCoupon.setText("APPLY");
                couponInput.setEnabled(true);

                Toast.makeText(this, "Coupon Removed ", Toast.LENGTH_SHORT).show();
                return;
            }

            // apply logic
            String code = couponInput.getText().toString().trim();

            if (code.equalsIgnoreCase("SAVE10")) {
                discount = total * 0.10;
            }
            else if (code.equalsIgnoreCase("FLAT100")) {
                discount = 100;
            }
            else {
                Toast.makeText(this, "Invalid Coupon ", Toast.LENGTH_SHORT).show();
                return;
            }

            double finalTotal = total - discount;

            discountText.setText("Discount: ₹ " + String.format("%.0f", discount));
            totalPrice.setText("Total: ₹ " + String.format("%.0f", finalTotal));

            isCouponApplied = true;

            applyCoupon.setText("REMOVE");
            couponInput.setEnabled(false);

            Toast.makeText(this, "Coupon Applied ", Toast.LENGTH_SHORT).show();
        });

        // Checkout
        checkoutBtn.setOnClickListener(v -> {

            double finalTotal = isCouponApplied ? (total - discount) : total;

            Intent intent = new Intent(Activity_Cart.this, AddressActivity.class); // ✅ FIX

            intent.putExtra("total", finalTotal);

            startActivity(intent);
        });
    }

    private void loadCart() {
        list = dbHelper.getCartItems();

        adapter = new CartAdapter(this, list);
        recyclerView.setAdapter(adapter);

        total = dbHelper.getTotalPrice();
        totalPrice.setText("Total: ₹ " + total);

        discountText.setText("Discount: ₹ 0");

        // Reset coupon on reload
        isCouponApplied = false;
        applyCoupon.setText("APPLY");
        couponInput.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }
}