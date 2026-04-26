package com.example.shoppingapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shoppingapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "ShopDB", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price REAL, image INTEGER)");
        db.execSQL("CREATE TABLE cart(id INTEGER PRIMARY KEY AUTOINCREMENT, product_name TEXT, price REAL, quantity INTEGER, image INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS products");
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }

    // 🛍 Insert products
    public void insertProduct(String name, double price, int image) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO products(name,price,image) VALUES(?,?,?)",
                new Object[]{name, price, image});
    }

    // 📦 Get products
    public List<Product> getProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        while (cursor.moveToNext()){
            list.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    1, // default quantity
                    cursor.getInt(3) // 👈 image
            ));
        }

        cursor.close();
        return list;
    }

    // 🛒 Add to cart
    public void addToCart(String name, double price, int image) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM cart WHERE product_name=?", new String[]{name});

        if (cursor.moveToFirst()) {
            int qty = cursor.getInt(3) + 1;
            db.execSQL("UPDATE cart SET quantity=? WHERE product_name=?", new Object[]{qty, name});
        } else {
            db.execSQL("INSERT INTO cart(product_name,price,quantity,image) VALUES(?,?,1,?)",
                    new Object[]{name, price, image});
        }

        cursor.close();
    }

    // 📥 Get cart items
    public List<Product> getCartItems() {
        List<Product> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);

        while (cursor.moveToNext()) {
            list.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getDouble(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            ));
        }
        cursor.close();
        return list;
    }

    // ➕ Increase quantity
    public void increaseQty(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE cart SET quantity = quantity + 1 WHERE product_name=?", new Object[]{name});
    }

    // ➖ Decrease quantity
    public void decreaseQty(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE cart SET quantity = quantity - 1 WHERE product_name=?", new Object[]{name});
        db.execSQL("DELETE FROM cart WHERE quantity <= 0");
    }

    // 🗑 Delete item
    public void deleteItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart WHERE product_name=?", new Object[]{name});
    }

    public boolean isProductExists(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM cart WHERE product_name=?",
                new String[]{name}
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        return exists;
    }

    // 💰 Total price
    public double getTotalPrice() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(price * quantity) FROM cart", null);

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        return total;
    }
    // check table empty
    public boolean isProductTableEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        boolean empty = cursor.getCount() == 0;
        cursor.close();

        return empty;
    }
    // 🧹 Clear cart (checkout के बाद)
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart");
    }
}