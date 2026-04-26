package com.example.shoppingapp.model;

public class Product {

    int id;
    String name;
    double price;
    int quantity;
    int image;

    public Product(int id, String name, double price, int quantity,int image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image=image;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getImage() {

        return image;

    }
}