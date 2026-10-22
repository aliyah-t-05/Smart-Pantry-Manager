# Git / GitHub plan (minimum 10 commits - do them over several days, NOT in one sitting)

Your lecturer checks that your history is genuine and matches your video. Commit after each real step.

```
git init
git branch -M main
git remote add origin https://github.com/<you>/SmartPantryManager.git
```

| # | Step you actually finish | Example commit message |
|---|--------------------------|------------------------|
| 1 | New Android Studio project, builds & runs | `Create empty Java project with Material theme` |
| 2 | Model classes | `Add PantryItem, Recipe and RecipeIngredient models` |
| 3 | DatabaseHelper tables + pantry CRUD | `Implement DatabaseHelper with pantry CRUD methods` |
| 4 | Pantry list screen | `Add MainActivity layout and PantryAdapter with RecyclerView` |
| 5 | Add/Edit screen + validation | `Add AddEditItemActivity with input validation` |
| 6 | Delete with confirmation | `Add delete confirmation dialog on pantry items` |
| 7 | Recipe seed data | `Seed 22 recipes and ingredients on first run` |
| 8 | Matching logic | `Implement strict-matching rule in RecipeMatcher` |
| 9 | Normalizer + units | `Normalize names and convert units so tomato matches tomatoes` |
| 10 | Suggested screen + empty state | `Add SuggestedRecipesActivity with empty-state message` |
| 11 | Recipe detail | `Add RecipeDetailActivity with ingredient tick/cross` |
| 12 | Settings + expiry banner | `Add settings screen and expiring-soon banner` |
| 13 | Bonus + tests | `Add Almost There list and RecipeMatcher unit tests` |
| 14 | Bug fix you really hit | `Fix list not refreshing after edit (reload in onResume)` |
| 15 | Docs | `Write README with setup instructions` |

Push after each commit: `git add . && git commit -m "..." && git push -u origin main`
