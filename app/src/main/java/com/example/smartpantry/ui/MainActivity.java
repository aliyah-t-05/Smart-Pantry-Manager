package com.example.smartpantry.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.ExpiryUtils;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/** Screen 1 - the Pantry List (READ + entry points to CREATE, UPDATE, DELETE). */
public class MainActivity extends BaseActivity implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private RecyclerView rvPantry;
    private TextView tvEmpty;
    private TextView tvBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.menu_pantry);

        db = new DatabaseHelper(this);

        rvPantry = findViewById(R.id.rvPantry);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvBanner = findViewById(R.id.tvExpiryBanner);

        adapter = new PantryAdapter(this);
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        rvPantry.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        // Explicit Intent: go to the Add/Edit screen with no extras = "add mode"
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditItemActivity.class)));
        
    }

    /**
     * onResume runs every time this screen comes back to the front (e.g. after saving an item
     * on the Add/Edit screen), so the list is always fresh from the database.
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean alertsOn = prefs.getBoolean(SettingsActivity.KEY_ALERTS, true);
        int days = prefs.getInt(SettingsActivity.KEY_DAYS, 3);

        List<PantryItem> items = db.getAllItems();
        adapter.setData(items, alertsOn, days);

        boolean empty = items.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rvPantry.setVisibility(empty ? View.GONE : View.VISIBLE);

        // Expiring-soon banner (only when the Settings toggle is on)
        int soon = 0;
        for (PantryItem item : items) {
            if (ExpiryUtils.isExpiringSoon(item.getExpiryDate(), days)) soon++;
        }
        if (alertsOn && soon > 0) {
            tvBanner.setText(soon + (soon == 1 ? " item is" : " items are")
                    + " expiring within " + days + (days == 1 ? " day" : " days") + " - cook them soon!");
            tvBanner.setVisibility(View.VISIBLE);
        } else {
            tvBanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(PantryItem item) {
        // Intent with an extra: tells the next screen WHICH row to edit
        Intent intent = new Intent(this, AddEditItemActivity.class);
        intent.putExtra(AddEditItemActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton(R.string.confirm_yes, (dialog, which) -> {
                    db.deleteItem(item.getId());
                    Toast.makeText(this, item.getName() + " deleted", Toast.LENGTH_SHORT).show();
                    refreshList();
                })
                .setNegativeButton(R.string.confirm_no, null)
                .show();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
