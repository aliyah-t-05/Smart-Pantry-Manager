package com.example.smartpantry.logic;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THE CORE BUSINESS LOGIC: the strict-matching rule (assignment section 2.3).
 *
 * A recipe is "suggested" only if EVERY ingredient it needs is in the pantry in at least the
 * required quantity. One missing or insufficient ingredient and the recipe is excluded.
 *
 * Robustness:
 *  - names are normalised (case, spacing, singular/plural) by IngredientNormalizer
 *  - quantities are converted to a common base unit by UnitConverter (kg vs g, tbsp vs ml ...)
 *  - if the pantry holds the same ingredient on several rows, the quantities are added together
 */
public final class RecipeMatcher {

    private static final double EPSILON = 0.0001;

    private RecipeMatcher() { }

    /** The outcome of testing one recipe against the pantry. */
    public static class MatchResult {
        private final Recipe recipe;
        private final List<String> missing;

        MatchResult(Recipe recipe, List<String> missing) {
            this.recipe = recipe;
            this.missing = missing;
        }

        public Recipe getRecipe() { return recipe; }
        /** Names of ingredients that are absent or present in too small a quantity. */
        public List<String> getMissing() { return missing; }
        public boolean isStrictMatch() {
            return missing.isEmpty() && !recipe.getIngredients().isEmpty();
        }
    }

    /** Key = normalised name + family (count vs amount), so "2 pcs egg" never satisfies "200 g egg". */
    private static String key(String name, String unit) {
        return IngredientNormalizer.normalize(name) + "|" + (UnitConverter.isCount(unit) ? "C" : "A");
    }

    /** Tests a single recipe against the pantry. */
    public static MatchResult evaluate(Recipe recipe, List<PantryItem> pantry) {
        // 1. Total up what the user has, per ingredient
        Map<String, Double> have = new HashMap<>();
        for (PantryItem item : pantry) {
            if (item.getQuantity() <= 0) continue;
            String k = key(item.getName(), item.getUnit());
            double base = UnitConverter.toBase(item.getQuantity(), item.getUnit());
            Double current = have.get(k);
            have.put(k, (current == null ? 0 : current) + base);
        }

        // 2. Check every required ingredient
        List<String> missing = new ArrayList<>();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            double need = UnitConverter.toBase(ing.getQuantity(), ing.getUnit());
            Double got = have.get(key(ing.getName(), ing.getUnit()));
            if (got == null || got + EPSILON < need) {
                missing.add(ing.getName());
            }
        }
        return new MatchResult(recipe, missing);
    }

    /** Recipes the user can cook right now: nothing missing at all. */
    public static List<MatchResult> strictMatches(List<Recipe> recipes, List<PantryItem> pantry) {
        List<MatchResult> out = new ArrayList<>();
        for (Recipe r : recipes) {
            MatchResult m = evaluate(r, pantry);
            if (m.isStrictMatch()) out.add(m);
        }
        return out;
    }

    /** BONUS list: recipes missing exactly one ingredient. Always shown separately from strict matches. */
    public static List<MatchResult> almostThere(List<Recipe> recipes, List<PantryItem> pantry) {
        List<MatchResult> out = new ArrayList<>();
        for (Recipe r : recipes) {
            MatchResult m = evaluate(r, pantry);
            if (m.getMissing().size() == 1) out.add(m);
        }
        return out;
    }
}
