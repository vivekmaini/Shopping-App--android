package com.example.shoppingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Product;
import com.example.shoppingapp.database.DBHelper;
import android.widget.ImageView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<Product> list;
    Context context;


    public CartAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, price, qty;
        Button plusBtn, minusBtn, deleteBtn;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            qty = itemView.findViewById(R.id.qty);

            plusBtn = itemView.findViewById(R.id.plusBtn);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            image =itemView.findViewById(R.id.productImage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = list.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("₹ " + product.getPrice());
        holder.qty.setText(String.valueOf(product.getQuantity()));
        holder.image.setImageResource(product.getImage());

        // ➕ Increase
        holder.plusBtn.setOnClickListener(v -> {
            DBHelper db = new DBHelper(context);
            db.increaseQty(product.getName());

            product.setQuantity(product.getQuantity() + 1);
            notifyItemChanged(position);
            ((android.app.Activity) context).recreate();
        });

        // ➖ Decrease
        holder.minusBtn.setOnClickListener(v -> {
            DBHelper db = new DBHelper(context);
            db.decreaseQty(product.getName());

            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                notifyItemChanged(position);
            }
            ((android.app.Activity) context).recreate();
        });

        // 🗑 Delete
        holder.deleteBtn.setOnClickListener(v -> {
            DBHelper db = new DBHelper(context);
            db.deleteItem(product.getName());

            list.remove(position);
            notifyItemRemoved(position);
            ((android.app.Activity) context).recreate();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}