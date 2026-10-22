# Video plan (5-7 minutes, MP4 H.264, 1080p, your VOICE narrating throughout)

| Time | What is on screen | What to say (in YOUR words) |
|------|-------------------|-----------------------------|
| 0:00-1:00 | GitHub repo > Commits page, scroll slowly | How the project grew: database first, then screens, then matching, then polish |
| 1:00-3:30 | Emulator/phone | Add 3 ingredients (**Create**) > view list (**Read**) > tap one, change quantity (**Update**) > Delete one with the dialog. Show a validation error (empty name / quantity 0). |
| | | **Prove strict matching:** pantry has bread + cheese only -> open Suggested: Cheese Toastie is NOT there. Add butter -> it appears. Delete butter -> it disappears. Tick the "Almost There" box to show it listed separately. Show the empty-state message. |
| | | Close the app fully (recents swipe) and reopen: data still there (persistence). |
| 3:30-6:00 | Android Studio, code open | Explain THREE of: (a) strict-matching algorithm in `RecipeMatcher.evaluate` (b) RecyclerView + `PantryAdapter` (c) Intents with extras (`EXTRA_ITEM_ID`) (d) `onResume` reloading data (lifecycle) (e) SQLite end-to-end. Point at the actual lines. |
| 6:00-6:45 | Code / README | Why SQLite: offline, local, no server, simple for a personal app |

Tips: practise once; stay under 7:00; don't read a script word for word - you must be able to explain it yourself.
