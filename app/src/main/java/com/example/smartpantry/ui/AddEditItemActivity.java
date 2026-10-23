package com.example.smartpantry.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.UnitConverter;
import com.example.smartpantry.model.PantryItem;

import java.util.Calendar;
import java.util.Locale;

/**
 * Screen 2 - Add / Edit Ingredient (CREATE and UPDATE).
 * The same screen is used for both: if the launching Intent carries EXTRA_ITEM_ID we are editing.
 */
public class AddEditItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";

    private DatabaseHelper db;
    private EditText etName, etQuantity, etExpiry;
    private Spinner spUnit;
    private long itemId = -1; // -1 = adding a new item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnClearDate = findViewById(R.id.btnClearDate);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back arrow
            
        }

        spUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, UnitConverter.UNITS));

        // Edit mode? Read the id that MainActivity put into the Intent.
        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            setTitle(R.string.title_edit);
            loadItem();
        } else {
            setTitle(R.string.title_add);
        }

        etExpiry.setOnClickListener(v -> showDatePicker());
        btnClearDate.setOnClickListener(v -> etExpiry.setText(""));
        btnSave.setOnClickListener(v -> saveItem());
    }

    private void loadItem() {
        PantryItem item = db.getItem(itemId);
        if (item == null) { // row vanished - nothing to edit
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        etName.setText(item.getName());
        etQuantity.setText(UnitConverter.formatQuantity(item.getQuantity()));
        etExpiry.setText(item.getExpiryDate() == null ? "" : item.getExpiryDate());
        for (int i = 0; i < UnitConverter.UNITS.length; i++) {
            if (UnitConverter.UNITS[i].equals(item.getUnit())) {
                spUnit.setSelection(i);
                break;
            }
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) ->
                etExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    /** Validates the form; only writes to the database if every rule passes. */
    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyText = etQuantity.getText().toString().trim();
        boolean valid = true;

        // Rule 1: name required, at least 2 characters, letters/spaces/hyphens only
        if (name.isEmpty()) {
            etName.setError("Please enter an ingredient name");
            valid = false;
        } else if (name.length() < 2) {
            etName.setError("Name must be at least 2 characters");
            valid = false;
        } else if (!name.matches("[\\p{L} \\-']+")) {
            etName.setError("Use letters only (no numbers or symbols)");
            valid = false;
        }

        // Rule 2: quantity required, a valid number, greater than zero
        double quantity = 0;
        if (qtyText.isEmpty()) {
            etQuantity.setError("Please enter a quantity");
            valid = false;
        } else {
            try {
                quantity = Double.parseDouble(qtyText);
                if (quantity <= 0) {
                    etQuantity.setError("Quantity must be greater than 0");
                    valid = false;
                }
            } catch (NumberFormatException e) {
                etQuantity.setError("Enter a valid number");
                valid = false;
            }
        }

        if (!valid) return; // stay on the form so the user can correct it

        String unit = (String) spUnit.getSelectedItem();
        String expiry = etExpiry.getText().toString().trim();
        PantryItem item = new PantryItem(itemId, name, quantity, unit, expiry.isEmpty() ? null : expiry);

        if (itemId == -1) {
            db.addItem(item);                       // CREATE
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            db.updateItem(item);                    // UPDATE
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish(); // back to the pantry list (its onResume reloads the data)
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // the back arrow in the action bar
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
