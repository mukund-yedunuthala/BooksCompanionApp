# Architecture

## Application flow

```text
MainActivity
  └── NavGraph (theme selection and navigation)
        ├── HomeScreen ───────────── BooksViewModel
        ├── Overview ─────────────── BooksViewModel
        ├── Backup_Screen ────────── BooksViewModel
        └── SettingScreen ────────── SettingsViewModel

BooksViewModel ── BooksDao ── BooksDatabase (Room / SQLite, schema v5)
      └────────── DataStore<Preferences> (sort option)

SettingsViewModel ── DataStore<Preferences> (theme preferences)
```

`BookCompanionApp` is the Hilt application entry point, and `MainActivity` is the Android activity entry point. `NavGraph` owns the navigation host and resolves the theme using `resolveTheme(followSystem, userDark, isSystemDark)`.

## Backend layers

- **Domain model:** `domain/model/Book.kt` defines the `Book` Room entity. The entity fields are the persisted book record.
- **Persistence:** `data/network/BooksDao.kt` defines Room queries and writes; `BooksDatabase.kt` declares the database and migrations.
- **Dependency injection:** `di/AppModule.kt` creates singleton `BooksDatabase`, `BooksDao`, and `DataStore<Preferences>` instances.
- **Application state:** `ui/home/HomeViewModel.kt` injects `BooksDao` directly and owns book-list/detail state. `ui/settings/SettingsViewModel.kt` owns theme preference state.
- **Backup:** `ui/settings/backup/BackupScreen.kt` reads and writes JSON through Android document/content resolver APIs.

The current code has no `BooksRepository`, repository implementation, or separate database provider. See [Backend reference](backend-reference.md) for DAO contracts, migrations, preference keys, backup format, and testing locations.

## Theme resolution

Theme selection is resolved in `navigation/NavGraph.kt`:

```kotlin
fun resolveTheme(followSystem: Boolean, userDark: Boolean, isSystemDark: Boolean): Boolean =
    if (followSystem) isSystemDark else userDark
```

`SettingsViewModel` reads and writes the preferences that supply `followSystem` and `userDark`.
