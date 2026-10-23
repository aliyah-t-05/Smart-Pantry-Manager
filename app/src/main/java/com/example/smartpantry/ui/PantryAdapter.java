package com.example.smartpantry.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.logic.ExpiryUtils;
import com.example.smartpantry.logic.UnitConverter;
import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/** Custom adapter that binds PantryItem objects (from SQLite) to item_pantry.xml rows. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    /** The Activity implements this so the adapter never needs to know about databases or Intents. */
    public interface Listener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean alertsEnabled = true;
    private int alertDays = 3;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
        
    }

    public void setData(List<PantryItem> newItems, boolean alertsEnabled, int alertDays) {
        items.clear();
        items.addAll(newItems);
        this.alertsEnabled = alertsEnabled;
        this.alertDays = alertDays;
        notifyDataSetChanged();
        
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PantryItem item = items.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQty.setText(UnitConverter.formatQuantity(item.getQuantity()) + " " + item.getUnit());

        String expiry = item.getExpiryDate();
        if (expiry == null || expiry.isEmpty()) {
            holder.tvExpiry.setText("No expiry date");
            holder.tvExpiry.setTextColor(Color.GRAY);
        } else if (alertsEnabled && ExpiryUtils.isExpiringSoon(expiry, alertDays)) {
            holder.tvExpiry.setText("Expires: " + expiry + "  (use soon!)");
            holder.tvExpiry.setTextColor(Color.parseColor("#C62828"));
        } else {
            holder.tvExpiry.setText("Expires: " + expiry);
            holder.tvExpiry.setTextColor(Color.GRAY);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvQty, tvExpiry;
        final Button btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvExpiry = itemView.findViewById(R.id.tvExpiry);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
