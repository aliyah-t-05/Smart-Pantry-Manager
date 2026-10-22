package com.example.smartpantry.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.logic.UnitConverter;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.HashSet;
import java.util.Set;

/** Screen 4 - Recipe Detail: full ingredient list (with a tick/cross per ingredient) and the method. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setTitle(R.string.title_recipe);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            db.close();
            finish();
            return;
        }

        // Which ingredients does the user still lack? (empty for a strict suggestion)
        Set<String> missing = new HashSet<>(
                RecipeMatcher.evaluate(recipe, db.getAllItems()).getMissing());
        db.close();

        ((TextView) findViewById(R.id.tvRecipeTitle)).setText(recipe.getName());

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            sb.append(missing.contains(ing.getName()) ? "\u2717  " : "\u2713  ")
              .append(UnitConverter.formatQuantity(ing.getQuantity())).append(' ')
              .append(ing.getUnit()).append("  ")
              .append(ing.getName()).append('\n');
        }
        ((TextView) findViewById(R.id.tvIngredients)).setText(sb.toString().trim());
        ((TextView) findViewById(R.id.tvMethod)).setText(recipe.getSteps());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
