package com.example.smartpantry.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;

/**
 * Parent of the three "top level" screens (Pantry, Suggested, Settings).
 * It provides the toolbar overflow menu used for navigation between them,
 * so the menu code is written once instead of three times.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.action_pantry) {
            intent = new Intent(this, MainActivity.class);
            // reuse the existing pantry screen instead of stacking a new copy
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else if (id == R.id.action_suggested) {
            intent = new Intent(this, SuggestedRecipesActivity.class);
        } else if (id == R.id.action_settings) {
            intent = new Intent(this, SettingsActivity.class);
        } else {
            return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }
}
