package com.example.smartpantry.logic;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Converts quantities to a common base so that "1 kg" in the pantry satisfies "500 g" in a recipe.
 *
 * Two families:
 *  - AMOUNT: g, kg, ml, l, tsp, tbsp, cup  (base = g/ml; we treat 1 g ~ 1 ml, a simplification)
 *  - COUNT : pcs (eggs, onions, bread slices, cans ...)
 */
public final class UnitConverter {

    /** Units offered in the Add/Edit spinner. */
    public static final String[] UNITS = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};

    private static final Map<String, String> ALIASES = new HashMap<>();
    static {
        String[][] a = {
                {"gram", "g"}, {"grams", "g"}, {"gr", "g"},
                {"kilogram", "kg"}, {"kilograms", "kg"}, {"kgs", "kg"},
                {"milliliter", "ml"}, {"milliliters", "ml"}, {"millilitre", "ml"}, {"millilitres", "ml"},
                {"liter", "l"}, {"liters", "l"}, {"litre", "l"}, {"litres", "l"},
                {"teaspoon", "tsp"}, {"teaspoons", "tsp"},
                {"tablespoon", "tbsp"}, {"tablespoons", "tbsp"},
                {"cups", "cup"},
                {"piece", "pcs"}, {"pieces", "pcs"}, {"pc", "pcs"}, {"unit", "pcs"}, {"units", "pcs"},
                {"whole", "pcs"}, {"slice", "pcs"}, {"slices", "pcs"}, {"can", "pcs"}, {"clove", "pcs"}
        };
        for (String[] pair : a) ALIASES.put(pair[0], pair[1]);
        
    }

    private UnitConverter() { }

    public static String normalizeUnit(String unit) {
        if (unit == null) return "pcs";
        String u = unit.trim().toLowerCase(Locale.ROOT);
        if (u.isEmpty()) return "pcs";
        return ALIASES.containsKey(u) ? ALIASES.get(u) : u;
    }

    /** True when the unit counts whole things rather than measuring an amount. */
    public static boolean isCount(String unit) {
        return "pcs".equals(normalizeUnit(unit));
    }

    /** Converts a quantity to its base unit (g/ml for amounts, pieces for counts). */
    public static double toBase(double quantity, String unit) {
        switch (normalizeUnit(unit)) {
            case "kg":   return quantity * 1000;
            case "l":    return quantity * 1000;
            case "tsp":  return quantity * 5;
            case "tbsp": return quantity * 15;
            case "cup":  return quantity * 240;
            default:     return quantity; // g, ml, pcs
        }
    }

    /** 3.0 -> "3", 2.5 -> "2.5" (nicer on screen). */
    public static String formatQuantity(double q) {
        DecimalFormat df = new DecimalFormat("0.##", DecimalFormatSymbols.getInstance(Locale.US));
        return df.format(q);
    }
}
