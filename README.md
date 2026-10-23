# Smart Pantry Manager

It is a Java Android app that helps reduce food waste. You tell it what ingredients you already have at home
(your "pantry") and it suggests recipes you can cook **using strictly those ingredients** - no shopping trip needed.

## Features
- Pantry management: add, edit and delete ingredients (name, quantity, unit, optional expiry date)
- Pantry list (RecyclerView + custom adapter) read from the database
- 22 pre-loaded recipes, seeded on first run
- **Suggested Recipes** screen using the strict-matching rule: a recipe appears only if *every* ingredient is
  in the pantry in at least the required quantity
- Robust matching: case, spacing, singular/plural ("tomato" = "Tomatoes") and unit conversion (1 kg satisfies 200 g)
- Bonus: separate "Almost There" list (recipes missing exactly 1 ingredient)
- Recipe detail screen with tick/cross per ingredient and the method
- Settings screen: expiring-soon alerts on/off and warning window (1/3/7 days)
- Input validation on the Add/Edit form; friendly message when no recipes match
- No Google Maps, no location/GPS, no internet permission - 100% offline

## Database choice: SQLite (SQLiteOpenHelper)
SQLite was chosen because the app is entirely personal and offline: the pantry and recipes live on the device,
there is no server to build or pay for, and the data survives the app being closed. Three tables:
`pantry`, `recipes`, `recipe_ingredients` (FK -> recipes, ON DELETE CASCADE).

## Setup / run instructions

1. Install **Android Studio** (Hedgehog or newer) with an Android SDK (API 34).
2. Clone the repo: `git clone <your-repo-url>`
3. In Android Studio choose **File > Open** and select the project folder; wait for Gradle sync to finish.
4. Create an emulator (e.g. Pixel 6, API 34) in **Device Manager**, or plug in a phone with USB debugging.
5. Press **Run** (green triangle).
6. To run the unit tests: right-click `app/src/test/.../RecipeMatcherTest` > **Run**.

## Project structure
```
app/src/main/java/com/example/smartpantry/
  model/  PantryItem, Recipe, RecipeIngredient          (plain data classes)
  logic/  RecipeMatcher, IngredientNormalizer,           (strict-matching business logic, no Android code)
          UnitConverter, ExpiryUtils
  db/     DatabaseHelper (SQLite CRUD), SeedData (22 recipes)
  ui/     MainActivity, AddEditItemActivity, SuggestedRecipesActivity,
          RecipeDetailActivity, SettingsActivity, BaseActivity, PantryAdapter, RecipeAdapter
```

## Known limitations
- Matching handles case, plurals and units, but not descriptive words: "garlic cloves" will not match "garlic".
- Grams and millilitres are treated as equivalent (1 g ~ 1 ml); counted items (pcs) never match measured amounts.
