package com.example.smartpantry.db;

import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * The 22 recipes that are pre-loaded into the database on first run.
 * Kept free of Android classes so the matching logic can be tested against the real data.
 *
 * Ingredient format: "name:quantity:unit"  (units: g, kg, ml, l, tsp, tbsp, cup, pcs)
 */
public final class SeedData {

    private SeedData() { }

    public static List<Recipe> recipes() {
        List<Recipe> list = new ArrayList<>();

        add(list, "Scrambled Eggs on Toast",
                "1. Toast the bread.\n2. Beat the eggs with the salt.\n3. Melt the butter in a pan on low heat.\n4. Stir the eggs gently until just set, then serve on the toast.",
                "egg:3:pcs", "bread:2:pcs", "butter:10:g", "salt:1:tsp");
        add(list, "Cheese Omelette",
                "1. Beat the eggs with the salt.\n2. Melt the butter in a pan.\n3. Pour in the eggs and cook until almost set.\n4. Add the grated cheese, fold and serve.",
                "egg:3:pcs", "cheese:50:g", "butter:10:g", "salt:1:tsp");
        add(list, "Tomato Pasta",
                "1. Boil the pasta in salted water until tender.\n2. Fry crushed garlic in olive oil.\n3. Add chopped tomatoes and simmer for 10 minutes.\n4. Toss with the drained pasta.",
                "pasta:200:g", "tomato:4:pcs", "garlic:2:pcs", "olive oil:2:tbsp", "salt:1:tsp");
        add(list, "Garlic Butter Rice",
                "1. Cook the rice until fluffy.\n2. Melt the butter and fry the crushed garlic for 1 minute.\n3. Stir through the rice and season with salt.",
                "rice:1:cup", "butter:20:g", "garlic:2:pcs", "salt:1:tsp");
        add(list, "Egg Fried Rice",
                "1. Cook the rice and let it cool.\n2. Fry the chopped onion in oil.\n3. Push aside, scramble the eggs in the pan.\n4. Add rice and soy sauce and stir-fry for 3 minutes.",
                "rice:2:cup", "egg:2:pcs", "onion:1:pcs", "soy sauce:2:tbsp", "oil:2:tbsp");
        add(list, "Fluffy Pancakes",
                "1. Whisk flour, sugar, eggs and milk into a smooth batter.\n2. Melt a little butter in a pan.\n3. Pour small rounds of batter and cook until bubbles form.\n4. Flip and cook until golden.",
                "flour:200:g", "milk:300:ml", "egg:2:pcs", "sugar:2:tbsp", "butter:20:g");
        add(list, "Creamy Mashed Potatoes",
                "1. Peel, chop and boil the potatoes until soft.\n2. Drain well.\n3. Mash with butter, warm milk and salt.",
                "potato:4:pcs", "butter:30:g", "milk:100:ml", "salt:1:tsp");
        add(list, "Cheese Toastie",
                "1. Butter the outside of the bread.\n2. Place the cheese between the slices.\n3. Toast in a pan until golden on both sides.",
                "bread:2:pcs", "cheese:60:g", "butter:10:g");
        add(list, "Chicken Rice Bowl",
                "1. Season the chicken with salt and fry in oil until cooked through.\n2. Fry the sliced onion until soft.\n3. Cook the rice.\n4. Serve the chicken and onion over the rice.",
                "chicken breast:300:g", "rice:1:cup", "onion:1:pcs", "oil:1:tbsp", "salt:1:tsp");
        add(list, "Simple Vegetable Soup",
                "1. Chop the carrots, potatoes and onion.\n2. Add everything to a pot with the stock.\n3. Simmer for 25 minutes and season with salt.",
                "carrot:2:pcs", "potato:2:pcs", "onion:1:pcs", "vegetable stock:500:ml", "salt:1:tsp");
        add(list, "Banana Smoothie",
                "1. Peel the bananas.\n2. Blend with the milk and honey until smooth.\n3. Serve cold.",
                "banana:2:pcs", "milk:250:ml", "honey:1:tbsp");
        add(list, "Peanut Butter Banana Toast",
                "1. Toast the bread.\n2. Spread with peanut butter.\n3. Top with sliced banana.",
                "bread:2:pcs", "peanut butter:2:tbsp", "banana:1:pcs");
        add(list, "Garlic Tuna Pasta",
                "1. Boil the pasta.\n2. Fry the garlic in olive oil.\n3. Add the drained tuna and salt, then toss through the pasta.",
                "pasta:200:g", "tuna:1:pcs", "olive oil:1:tbsp", "garlic:1:pcs", "salt:1:tsp");
        add(list, "Egg Fried Noodles",
                "1. Cook the noodles and drain.\n2. Scramble the eggs in hot oil.\n3. Add noodles and soy sauce and toss for 2 minutes.",
                "noodle:200:g", "egg:2:pcs", "soy sauce:2:tbsp", "oil:1:tbsp");
        add(list, "Pap with Tomato Gravy",
                "1. Cook the maize meal in boiling water, stirring until thick (pap).\n2. Fry the onion in oil, add chopped tomatoes and salt.\n3. Simmer into a thick gravy and serve over the pap.",
                "maize meal:2:cup", "tomato:3:pcs", "onion:1:pcs", "oil:2:tbsp", "salt:1:tsp");
        add(list, "Baked Beans on Toast",
                "1. Toast the bread and butter it.\n2. Heat the baked beans in a pot.\n3. Pour over the toast.",
                "baked beans:1:pcs", "bread:2:pcs", "butter:10:g");
        add(list, "Honey Oatmeal",
                "1. Combine oats and milk in a pot.\n2. Simmer for 5 minutes, stirring.\n3. Drizzle with honey.",
                "oat:1:cup", "milk:250:ml", "honey:1:tbsp");
        add(list, "Quick Lentil Curry",
                "1. Fry the onion and garlic in oil.\n2. Add the curry powder, tomatoes and lentils.\n3. Add water and simmer for 25 minutes until soft.",
                "lentil:1:cup", "onion:1:pcs", "tomato:2:pcs", "garlic:2:pcs", "curry powder:2:tsp", "oil:1:tbsp");
        add(list, "Chicken Stir-Fry",
                "1. Slice the chicken, pepper and onion.\n2. Fry the chicken in oil until golden.\n3. Add the vegetables and soy sauce and stir-fry for 5 minutes.",
                "chicken breast:300:g", "bell pepper:1:pcs", "onion:1:pcs", "soy sauce:2:tbsp", "oil:1:tbsp");
        add(list, "French Toast",
                "1. Whisk the eggs, milk and sugar.\n2. Dip each slice of bread in the mixture.\n3. Fry in butter until golden on both sides.",
                "bread:4:pcs", "egg:2:pcs", "milk:100:ml", "sugar:1:tbsp", "butter:10:g");
        add(list, "Chickpea Salad",
                "1. Drain the chickpeas.\n2. Chop the tomatoes and onion.\n3. Combine with olive oil, lemon juice and salt.",
                "chickpea:1:pcs", "tomato:2:pcs", "onion:1:pcs", "olive oil:2:tbsp", "lemon:1:pcs", "salt:1:tsp");
        add(list, "Spicy Potato Wedges",
                "1. Cut the potatoes into wedges.\n2. Toss with oil, salt and paprika.\n3. Bake at 200 C for 35 minutes, turning once.",
                "potato:4:pcs", "oil:2:tbsp", "salt:1:tsp", "paprika:1:tsp");

        return list;
        
    }

    private static void add(List<Recipe> list, String name, String steps, String... ingredients) {
        Recipe r = new Recipe(0, name, steps);
        for (String line : ingredients) {
            String[] p = line.split(":");
            r.addIngredient(new RecipeIngredient(p[0], Double.parseDouble(p[1]), p[2]));
        }
        list.add(r);
    }
}
