package com.example.smartpantry.logic;

import java.util.Locale;

/**
 * Makes ingredient names comparable so that trivial differences do not break matching:
 * "Tomatoes", " tomato " and "TOMATO" all become "tomato".
 * Plain Java (no Android classes) so it can be unit tested on the JVM.
 */
public final class IngredientNormalizer {

    private IngredientNormalizer() { }

    public static String normalize(String raw) {
        if (raw == null) return "";
        // lower-case, drop anything that is not a letter, collapse whitespace
        String s = raw.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (s.isEmpty()) return s;

        // Only the LAST word is made singular: "cherry tomatoes" -> "cherry tomato"
        int lastSpace = s.lastIndexOf(' ');
        String head = lastSpace < 0 ? "" : s.substring(0, lastSpace + 1);
        String last = lastSpace < 0 ? s : s.substring(lastSpace + 1);
        return head + singular(last);
        
    }

    /** Very small singularisation rule set - enough for pantry food names, not full NLP. */
    static String singular(String w) {
        if (w.length() <= 3) return w;
        if (w.endsWith("ies")) return w.substring(0, w.length() - 3) + "y";      // berries -> berry
        if (w.endsWith("oes")) return w.substring(0, w.length() - 2);            // tomatoes -> tomato
        if (w.endsWith("ches") || w.endsWith("shes") || w.endsWith("xes")) {
            return w.substring(0, w.length() - 2);                                // peaches -> peach
        }
        if (w.endsWith("ss") || w.endsWith("us")) return w;                       // hummus, couscous
        if (w.endsWith("s")) return w.substring(0, w.length() - 1);              // eggs -> egg
        return w;
    }
}
