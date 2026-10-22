package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.logic.RecipeMatcher;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;

import java.util.List;

/**
 * Screen 3 - Suggested Recipes.
 * Runs the strict-matching rule against the CURRENT pantry every time the screen is shown.
 */
public class SuggestedRecipesActivity extends BaseActivity implements RecipeAdapter.Listener {

    private DatabaseHelper db;
    private RecipeAdapter strictAdapter;
    private RecipeAdapter almostAdapter;
    private RecyclerView rvStrict, rvAlmost;
    private TextView tvEmpty, tvAlmostHeader;
    private CheckBox chkAlmost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested);
        setTitle(R.string.menu_suggested);

        db = new DatabaseHelper(this);
        rvStrict = findViewById(R.id.rvStrict);
        rvAlmost = findViewById(R.id.rvAlmost);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvAlmostHeader = findViewById(R.id.tvAlmostHeader);
        chkAlmost = findViewById(R.id.chkAlmost);

        strictAdapter = new RecipeAdapter(this, false);
        almostAdapter = new RecipeAdapter(this, true);
        rvStrict.setLayoutManager(new LinearLayoutManager(this));
        rvStrict.setAdapter(strictAdapter);
        rvAlmost.setLayoutManager(new LinearLayoutManager(this));
        rvAlmost.setAdapter(almostAdapter);

        // Bonus list: kept visually separate from the strict list
        chkAlmost.setOnCheckedChangeListener((button, checked) -> updateAlmostVisibility());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestions();
    }

    private void loadSuggestions() {
        List<PantryItem> pantry = db.getAllItems();
        List<Recipe> recipes = db.getAllRecipes();

        List<RecipeMatcher.MatchResult> strict = RecipeMatcher.strictMatches(recipes, pantry);
        strictAdapter.setData(strict);
        almostAdapter.setData(RecipeMatcher.almostThere(recipes, pantry));

        // Friendly feedback instead of a blank screen when nothing matches
        boolean none = strict.isEmpty();
        tvEmpty.setVisibility(none ? View.VISIBLE : View.GONE);
        rvStrict.setVisibility(none ? View.GONE : View.VISIBLE);
        updateAlmostVisibility();
    }

    private void updateAlmostVisibility() {
        int vis = chkAlmost.isChecked() ? View.VISIBLE : View.GONE;
        tvAlmostHeader.setVisibility(vis);
        rvAlmost.setVisibility(vis);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
