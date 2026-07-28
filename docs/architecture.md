# Architecture

## Data flow

```
MainActivity
  └── NavGraph (composition root — wires SettingsViewModel for theme)
        ├── HomeScreen ──────────────────── BooksViewModel
        ├── Overview (book detail/edit) ─── BooksViewModel
        ├── SettingScreen ───────────────── SettingsViewModel
        ├── BackupScreen ────────────────── BooksViewModel
        └── LibsScreen (static)

BooksViewModel
  └── BooksRepositoryImpl (implements BooksRepository)
        └── BooksDao (Room)
              └── BooksDatabase (SQLite, schema v3)

SettingsViewModel
  └── DataStore<Preferences> (theme persistence, file: "settings")
```

## Layers

### Domain (`domain/`)
The stable center. No Android framework imports beyond Room annotations.

- `Book` — the single Room entity. Fields: `id`, `title`, `author`, `year` (Long), `status`, `genre`, `isbn`.
- `BooksRepository` — interface defining all data operations. `BooksRepositoryImpl` depends on this, not the other way around.
- `Books` — typealias for `List<Book>`.

### Data (`data/`)
All I/O lives here. Must not import Compose or ViewModel.

- `BooksDao` — Room DAO. All methods are suspend-free; coroutine dispatch is handled by the ViewModel caller.
- `BooksDatabase` — `@Database(version = 3)`. Holds `MIGRATION_2_3`. `exportSchema = false`.
- `BookDbProvider` — provides the `BooksDatabase` instance via a `by lazy` delegate. Avoids multiple Room connections.
- `BooksRepositoryImpl` — one-line delegation wrappers around DAO calls. No logic lives here.

### DI (`di/`)
Single `AppModule` with `@Singleton` bindings:
- `BookDbProvider` ← `Application`
- `BooksRepository` ← `BooksRepositoryImpl(BookDbProvider)` — `BooksViewModel` injects the interface, not the concrete class
- `DataStore<Preferences>` ← `application.dataStore` (the `"settings"` file) — injected into `SettingsViewModel`

### UI (`ui/`)
Three screens with distinct ViewModel ownership:

| Screen | ViewModel | Scope |
|---|---|---|
| `HomeScreen` | `BooksViewModel` | Book list, add dialog |
| `Overview` | `BooksViewModel` | Single book detail, inline field editing |
| `BackupScreen` | `BooksViewModel` | Uses `viewModel.books` for export, `insertAllBooks` for import |
| `SettingScreen` | `SettingsViewModel` | Theme toggle switches |
| `LibsScreen` | none | Static OSS attribution list |

### Navigation (`navigation/`)

Routes are sealed objects in `Screen.kt`:

| Route | Destination |
|---|---|
| `books` | `HomeScreen` |
| `overview/{bookId}` | `Overview` — `bookId` is an `Int` nav argument |
| `settings` | `SettingScreen` |
| `libraries` | `LibsScreen` |
| `backup` | `BackupScreen` |

`NavGraph` is the **only** place theme is resolved. It reads `SettingsViewModel` and passes `darkTheme: Boolean` into `BooksCompanionTheme`. The logic is extracted into a pure top-level function for testability:

```kotlin
// navigation/NavGraph.kt
fun resolveTheme(followSystem: Boolean, userDark: Boolean, isSystemDark: Boolean): Boolean =
    if (followSystem) isSystemDark else userDark
```

```
systemTheme=true  → isSystemInDarkTheme() (ignores user toggle)
systemTheme=false → hasUserDarkThemeEnabled
```

### Design (`design/`)
Design tokens only. No Composables, no ViewModel references.

- `DesignTokens.kt` — color palettes, spacing scale, radius scale, border widths, opacity levels, animation timing.
- `ComposeFonts.kt` — three `FontFamily` definitions: `CormorantGaramond`, `IBMPlexSans`, `JetBrainsMono`.

`AppType` TextStyle constants live in `ui/theme/Type.kt` and reference the font families from `design/`.

## Key contracts

**DAO conflict strategies are intentionally asymmetric.**
`addBook` uses `IGNORE` (user taps "add" — a duplicate is a no-op, not an error).
`insertAll` uses `REPLACE` (backup restore — incoming data wins over existing records).

**`getBook(id)` returns `Flow<Book?>`.**
Nullable because the book may not exist yet at subscription time. `BooksViewModel.getBook` guards with `it?.let { book = it }` — the local `book` state is never nulled out once set.

**`updateStatus` trims whitespace; no other field updater does.**
Intentional — status values come from a controlled picker. Other fields accept user-typed input as-is.

**Room schema changes require a Migration object.**
`exportSchema = false` means there is no exported JSON to diff against. When the `Book` entity changes: (1) increment `version` in `@Database`, (2) add a `MIGRATION_n_(n+1)` object, (3) pass it to `addMigrations()` in `BookDbProvider`. Skipping any step silently destroys user data on app update.
