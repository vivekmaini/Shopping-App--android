package com.example.shoppingapp.activities;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingapp.R;
import com.example.shoppingapp.database.DBHelper;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView image;
    TextView name, price;
    Button addToCart;

    DBHelper dbHelper;

    String productName;
    double productPrice;
    int productImage;
    TextView desc, features;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        addToCart = findViewById(R.id.addToCart);
        desc = findViewById(R.id.desc);

        features = findViewById(R.id.features);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();

        productName = intent.getStringExtra("name");
        productPrice = intent.getDoubleExtra("price", 0);
        productImage = intent.getIntExtra("image", 0);

        name.setText(productName);
        price.setText("₹ " + productPrice);
        image.setImageResource(productImage);
        if (productName.equals("Shoes")) {

            desc.setText("Comfortable running shoes for daily use.");

            features.setText("• Lightweight\n• Breathable\n• Durable sole");

        }

        else if (productName.equals("T-Shirt")) {

            desc.setText("Stylish cotton t-shirt for casual wear.");

            features.setText("• 100% cotton\n• Soft fabric\n• Trendy design");

        }

        else {

            desc.setText("Premium quality product.");

            features.setText("• High quality\n• Long lasting\n• Best in class");

        }
        // ✅ ADD TO CART BUTTON
        addToCart.setOnClickListener(v -> {

            if (dbHelper.isProductExists(productName)) {
                dbHelper.increaseQty(productName);
            } else {
                dbHelper.addToCart(productName, productPrice, productImage);
            }

            Toast.makeText(ProductDetailActivity.this,
                    "Added to Cart 🛒",
                    Toast.LENGTH_SHORT).show();
        });
    }
}