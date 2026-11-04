# Repo Agent Guidance

**Build & APK**
- Compile debug APK: `./gradlew assembleDebug`. Output → `app/build/outputs/apk/debug/app-debug.apk`.
- Release build requires `keystore.properties` with `storeFile`, `storePassword`, `keyAlias`, `keyPassword`.

**Run & Install**
- Install APK on a connected device/emulator: `adb install -r app/build/outputs/apk/debug/app-debug.apk`.
- Launch app: `adb shell am start -n com.mukund.bookcompanion/.MainActivity`.

**Testing**
- Run all unit tests: `./gradlew :app:testDebugUnitTest`.
- Run a single test (replace fully‑qualified name): `./gradlew :app:testDebugUnitTest --tests "com.mukund.bookcompanion.ui.home.BooksViewModelGetBookTest"`.
- Android instrumented tests: `./gradlew connectedAndroidTest`.

**Architecture Overview**
- Entry point: `MainActivity` → `NavGraph` (provides theme via `SettingsViewModel`).
- Screens are composables wired to two ViewModels:
  - `BooksViewModel` (home, overview, backup) – injects `BooksDao` directly. There is **no** repository layer.
  - `SettingsViewModel` – uses `DataStore<Preferences>` for theme persistence.
- **Domain layer** (`domain/`): `Book` Room entity only (it imports Room).
- **Data layer** (`data/network/`): Room `BooksDao` and `BooksDatabase` (with migrations).
- **DI** (`di/AppModule.kt`): Hilt provides singleton `BooksDatabase`, `BooksDao`, and `DataStore`.
- **Navigation** (`navigation/NavGraph.kt` + `Screen` routes): books, overview/{bookId}, settings, libraries, backup.
- **Backup I/O** (`ui/settings/backup/BackupFile.kt`): JSON import/export, size limits; tested in `BackupFunctionsTest`.
- Theme resolution utility (pure function) lives in `navigation/NavGraph.kt` – use it in tests.

**Design System Rules**
- Use semantic color tokens from `DesignTokens.kt` via `LocalBookCompanionColors` (e.g., `colors.paper`).
- Do **not** reference raw hex values in Compose files.
- Text styles must come from `AppType` (defined in `ui/theme/Type.kt`).
- Spacing, radius, borders, opacity, and timing tokens are defined in `design/` – prefer them over hard‑coded `dp` values.
- Adding a new font requires updates to `ComposeFonts.kt` and `AppType`.

**Database Migration Checklist**
1. Increment `version` in `@Database` annotation (currently 5).
2. Add a `MIGRATION_n_(n+1)` object handling column changes.
3. Pass the migration to `addMigrations()` in `di/AppModule.kt`.
4. Extend `data/BooksDatabaseMigrationTest` so the new path is exercised – all 1→5 migrations must stay reachable.
5. **Do not** forget any step – a missing migration crashes every older install at launch (no fallback migration is configured).

**Theme Persistence**
- Settings stored in `DataStore<Preferences>` file named `"settings"`.
- Toggle in SettingsScreen updates `SettingsViewModel`; NavGraph reads it to resolve `darkTheme` via `resolveTheme(followSystem, userDark, isSystemDark)`.

**Common Pitfalls**
- Duplicate book insertion uses `IGNORE`; restore from backup uses `REPLACE` – be aware of conflict strategy when importing JSON.
- `BooksDao` methods are **suspend‑free**; coroutine context is handled by ViewModel.
- Room schema export disabled (`exportSchema = false`); rely on migration code, not generated JSON.

**CI / Lint / Formatting**
- No explicit CI files present; rely on standard Android lint (`./gradlew lint`).
- Code style follows Kotlin conventions; use Android Studio formatter.

**Helpful Scripts**
- `./gradlew clean` – clears build artifacts.
- `./gradlew :app:assembleDebug` – target specific module if multi‑module added later.

**References**
- Architecture details: `docs/architecture.md`.
- Design token usage: `docs/design-system.md`.
- Build config: `app/build.gradle.kts`.
- Theme resolution: `navigation/NavGraph.kt`.

*(End of guidance)*