package com.example.shoppingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.model.Product;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    Context context;
    List<Product> list;

    public InvoiceAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product p = list.get(position);

        holder.name.setText(p.getName() != null ? p.getName() : "Product");
        holder.price.setText("₹ " + p.getPrice());

        // 🔥 CLEAN IMAGE SET
        holder.image.setImageDrawable(null);

        if (p.getImage() != 0) {
            holder.image.setImageResource(p.getImage());
        } else {
            holder.image.setImageResource(R.drawable.shoes);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}