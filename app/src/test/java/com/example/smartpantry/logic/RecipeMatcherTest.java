package com.example.smartpantry.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/** Unit tests for the strict-matching rule (run with: Right-click > Run 'RecipeMatcherTest'). */
public class RecipeMatcherTest {

    private static PantryItem item(String name, double qty, String unit) {
        return new PantryItem(0, name, qty, unit, null);
        
    }

    /** A 5-ingredient recipe: tomato, onion, garlic, oil, salt */
    private static Recipe fiveIngredientRecipe() {
        Recipe r = new Recipe(1, "Test Sauce", "n/a");
        r.addIngredient(new RecipeIngredient("tomato", 3, "pcs"));
        r.addIngredient(new RecipeIngredient("onion", 1, "pcs"));
        r.addIngredient(new RecipeIngredient("garlic", 2, "pcs"));
        r.addIngredient(new RecipeIngredient("oil", 2, "tbsp"));
        r.addIngredient(new RecipeIngredient("salt", 1, "tsp"));
        return r;
    }

    @Test
    public void allIngredientsPresent_isSuggested() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("tomatoes", 4, "pcs"));
        pantry.add(item("Onion", 1, "pcs"));
        pantry.add(item("garlic", 2, "pcs"));
        pantry.add(item("oil", 100, "ml"));
        pantry.add(item("salt", 500, "g"));
        assertTrue(RecipeMatcher.evaluate(fiveIngredientRecipe(), pantry).isStrictMatch());
    }

    @Test
    public void fourOfFiveIngredients_mustNotBeSuggested() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("tomato", 4, "pcs"));
        pantry.add(item("onion", 1, "pcs"));
        pantry.add(item("garlic", 2, "pcs"));
        pantry.add(item("oil", 100, "ml"));
        // salt missing
        RecipeMatcher.MatchResult m = RecipeMatcher.evaluate(fiveIngredientRecipe(), pantry);
        assertFalse(m.isStrictMatch());
        assertEquals(1, m.getMissing().size());
        assertEquals("salt", m.getMissing().get(0));
    }

    @Test
    public void insufficientQuantity_mustNotBeSuggested() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("tomato", 2, "pcs")); // needs 3
        pantry.add(item("onion", 1, "pcs"));
        pantry.add(item("garlic", 2, "pcs"));
        pantry.add(item("oil", 100, "ml"));
        pantry.add(item("salt", 50, "g"));
        assertFalse(RecipeMatcher.evaluate(fiveIngredientRecipe(), pantry).isStrictMatch());
    }

    @Test
    public void duplicateRows_areAddedTogether() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("tomato", 2, "pcs"));
        pantry.add(item("Tomatoes", 1, "pcs")); // 2 + 1 = 3 -> enough
        pantry.add(item("onion", 1, "pcs"));
        pantry.add(item("garlic", 2, "pcs"));
        pantry.add(item("oil", 100, "ml"));
        pantry.add(item("salt", 50, "g"));
        assertTrue(RecipeMatcher.evaluate(fiveIngredientRecipe(), pantry).isStrictMatch());
    }

    @Test
    public void unitConversion_kgSatisfiesGrams() {
        Recipe r = new Recipe(2, "Flour thing", "n/a");
        r.addIngredient(new RecipeIngredient("flour", 200, "g"));
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("flour", 1, "kg"));
        assertTrue(RecipeMatcher.evaluate(r, pantry).isStrictMatch());
    }

    @Test
    public void countAndAmountAreNotInterchangeable() {
        Recipe r = new Recipe(3, "Egg thing", "n/a");
        r.addIngredient(new RecipeIngredient("egg", 2, "pcs"));
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(item("egg", 500, "g")); // grams of egg cannot stand in for 2 whole eggs
        assertFalse(RecipeMatcher.evaluate(r, pantry).isStrictMatch());
    }

    @Test
    public void normalizer_handlesPluralAndCase() {
        assertEquals("tomato", IngredientNormalizer.normalize("Tomatoes"));
        assertEquals("tomato", IngredientNormalizer.normalize("  TOMATO "));
        assertEquals("berry", IngredientNormalizer.normalize("berries"));
        assertEquals("cherry tomato", IngredientNormalizer.normalize("Cherry Tomatoes"));
        assertEquals("hummus", IngredientNormalizer.normalize("hummus"));
    }
}
