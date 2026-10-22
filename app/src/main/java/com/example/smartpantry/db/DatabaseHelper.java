package com.example.smartpantry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Local SQLite database (SQLiteOpenHelper).
 *
 * Tables:
 *   pantry             (id PK, name, quantity, unit, expiry)                -> user CRUD
 *   recipes            (id PK, name, steps)                                 -> seeded on first run
 *   recipe_ingredients (id PK, recipe_id FK -> recipes.id, name, quantity, unit)
 *
 * SQLite stores the file on the device, so the data survives closing/reopening the app.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    // pantry table
    private static final String T_PANTRY = "pantry";
    private static final String P_ID = "id";
    private static final String P_NAME = "name";
    private static final String P_QTY = "quantity";
    private static final String P_UNIT = "unit";
    private static final String P_EXPIRY = "expiry";

    // recipes table
    private static final String T_RECIPES = "recipes";
    private static final String R_ID = "id";
    private static final String R_NAME = "name";
    private static final String R_STEPS = "steps";

    // recipe_ingredients table
    private static final String T_ING = "recipe_ingredients";
    private static final String I_ID = "id";
    private static final String I_RECIPE = "recipe_id";
    private static final String I_NAME = "name";
    private static final String I_QTY = "quantity";
    private static final String I_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); // needed for ON DELETE CASCADE
    }

    /** Runs only once - the first time the database file is created. */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_PANTRY + " ("
                + P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + P_NAME + " TEXT NOT NULL, "
                + P_QTY + " REAL NOT NULL, "
                + P_UNIT + " TEXT NOT NULL, "
                + P_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + T_RECIPES + " ("
                + R_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + R_NAME + " TEXT NOT NULL, "
                + R_STEPS + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + T_ING + " ("
                + I_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + I_RECIPE + " INTEGER NOT NULL REFERENCES " + T_RECIPES + "(" + R_ID + ") ON DELETE CASCADE, "
                + I_NAME + " TEXT NOT NULL, "
                + I_QTY + " REAL NOT NULL, "
                + I_UNIT + " TEXT NOT NULL)");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_ING);
        db.execSQL("DROP TABLE IF EXISTS " + T_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + T_PANTRY);
        onCreate(db);
    }

    // ------------------------------------------------------------------ seeding

    private void seedRecipes(SQLiteDatabase db) {
        db.beginTransaction(); // one transaction = fast and all-or-nothing
        try {
            for (Recipe r : SeedData.recipes()) {
                ContentValues rv = new ContentValues();
                rv.put(R_NAME, r.getName());
                rv.put(R_STEPS, r.getSteps());
                long recipeId = db.insert(T_RECIPES, null, rv);

                for (RecipeIngredient ing : r.getIngredients()) {
                    ContentValues iv = new ContentValues();
                    iv.put(I_RECIPE, recipeId);
                    iv.put(I_NAME, ing.getName());
                    iv.put(I_QTY, ing.getQuantity());
                    iv.put(I_UNIT, ing.getUnit());
                    db.insert(T_ING, null, iv);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // ------------------------------------------------------------------ pantry CRUD

    /** CREATE */
    public long addItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(T_PANTRY, null, toValues(item));
    }

    /** READ (all) - sorted alphabetically for the list screen */
    public List<PantryItem> getAllItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_PANTRY, null, null, null, null, null, P_NAME + " COLLATE NOCASE ASC")) {
            while (c.moveToNext()) {
                items.add(fromCursor(c));
            }
        }
        return items;
    }

    /** READ (one) - used to pre-fill the edit form. Returns null if not found. */
    public PantryItem getItem(long id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_PANTRY, null, P_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null)) {
            return c.moveToFirst() ? fromCursor(c) : null;
        }
    }

    /** UPDATE */
    public int updateItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        return db.update(T_PANTRY, toValues(item), P_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    /** DELETE */
    public int deleteItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(T_PANTRY, P_ID + "=?", new String[]{String.valueOf(id)});
    }

    private ContentValues toValues(PantryItem item) {
        ContentValues v = new ContentValues();
        v.put(P_NAME, item.getName());
        v.put(P_QTY, item.getQuantity());
        v.put(P_UNIT, item.getUnit());
        if (item.getExpiryDate() == null) {
            v.putNull(P_EXPIRY);
        } else {
            v.put(P_EXPIRY, item.getExpiryDate());
        }
        return v;
    }

    private PantryItem fromCursor(Cursor c) {
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow(P_ID)),
                c.getString(c.getColumnIndexOrThrow(P_NAME)),
                c.getDouble(c.getColumnIndexOrThrow(P_QTY)),
                c.getString(c.getColumnIndexOrThrow(P_UNIT)),
                c.getString(c.getColumnIndexOrThrow(P_EXPIRY)));
    }

    // ------------------------------------------------------------------ recipes (read-only)

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_RECIPES, null, null, null, null, null, R_NAME + " ASC")) {
            while (c.moveToNext()) {
                recipes.add(recipeFromCursor(db, c));
            }
        }
        return recipes;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor c = db.query(T_RECIPES, null, R_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null)) {
            return c.moveToFirst() ? recipeFromCursor(db, c) : null;
        }
    }

    private Recipe recipeFromCursor(SQLiteDatabase db, Cursor c) {
        Recipe recipe = new Recipe(
                c.getLong(c.getColumnIndexOrThrow(R_ID)),
                c.getString(c.getColumnIndexOrThrow(R_NAME)),
                c.getString(c.getColumnIndexOrThrow(R_STEPS)));

        try (Cursor ic = db.query(T_ING, null, I_RECIPE + "=?",
                new String[]{String.valueOf(recipe.getId())}, null, null, I_ID + " ASC")) {
            while (ic.moveToNext()) {
                recipe.addIngredient(new RecipeIngredient(
                        ic.getString(ic.getColumnIndexOrThrow(I_NAME)),
                        ic.getDouble(ic.getColumnIndexOrThrow(I_QTY)),
                        ic.getString(ic.getColumnIndexOrThrow(I_UNIT))));
            }
        }
        return recipe;
    }
}
