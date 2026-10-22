package com.example.smartpantry.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;

import com.example.smartpantry.R;

/** Screen 5 - Settings, stored in SharedPreferences (small key/value settings, not the SQLite DB). */
public class SettingsActivity extends BaseActivity {

    public static final String PREFS_NAME = "smart_pantry_prefs";
    public static final String KEY_ALERTS = "alerts_enabled";
    public static final String KEY_DAYS = "alert_days";

    private static final int[] DAY_OPTIONS = {1, 3, 7}; // matches @array/alert_days_labels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.menu_settings);

        final SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        SwitchCompat switchAlerts = findViewById(R.id.switchAlerts);
        Spinner spDays = findViewById(R.id.spDays);

        spDays.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.alert_days_labels)));

        // Show the saved values
        switchAlerts.setChecked(prefs.getBoolean(KEY_ALERTS, true));
        int savedDays = prefs.getInt(KEY_DAYS, 3);
        for (int i = 0; i < DAY_OPTIONS.length; i++) {
            if (DAY_OPTIONS[i] == savedDays) spDays.setSelection(i);
        }
        spDays.setEnabled(switchAlerts.isChecked());

        // Save as soon as the user changes something
        switchAlerts.setOnCheckedChangeListener((button, checked) -> {
            prefs.edit().putBoolean(KEY_ALERTS, checked).apply();
            spDays.setEnabled(checked);
        });
        spDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putInt(KEY_DAYS, DAY_OPTIONS[position]).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
