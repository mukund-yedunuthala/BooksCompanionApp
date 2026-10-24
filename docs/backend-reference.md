# Backend Reference

This document describes the backend and persistence code currently in the app. The main implementation is in `app/src/main/java/com/mukund/bookcompanion/`.

## Structure and wiring

```text
BookCompanionApp (@HiltAndroidApp)
  └── MainActivity (@AndroidEntryPoint)
        └── NavGraph
              ├── BooksViewModel ── BooksDao ── BooksDatabase
              │        └────────── DataStore<Preferences> (book-list sort)
              └── SettingsViewModel ── DataStore<Preferences> (theme)
```

`di/AppModule.kt` is installed in Hilt's `SingletonComponent` and provides singleton instances:

| Provided value | Construction |
|---|---|
| `BooksDatabase` | `Room.databaseBuilder(application, BooksDatabase::class.java, BOOK_TABLE)` with migrations 2→3, 3→4, and 4→5 |
| `BooksDao` | `database.booksDao()` |
| `DataStore<Preferences>` | `application.dataStore`, the `Context.dataStore` delegate declared in `SettingsViewModel.kt` |

There is no repository interface/implementation or separate database-provider class in the current source. `BooksViewModel` receives `BooksDao` directly.

## Book entity

`domain/model/Book.kt` declares the Room entity `Book` for table `book_table` (`Constants.BOOK_TABLE`). Fields are:

| Field | Kotlin type | Persistence/default |
|---|---|---|
| `id` | `Int` | Auto-generated primary key |
| `title` | `String` | Required |
| `author` | `String` | Required |
| `year` | `Long` | Required |
| `status` | `String` | Required |
| `genre` | `String` | Required |
| `isbn` | `String` | Required |
| `rating` | `Int?` | Nullable, defaults to `null` |
| `notes` | `String?` | Nullable, defaults to `null` |
| `dateStarted` | `String?` | Nullable, defaults to `null` |
| `dateFinished` | `String?` | Nullable, defaults to `null` |
| `language` | `String?` | Nullable, defaults to `null` |

New book entry code supplies `id = 0`; Room's generated primary key behavior is configured by `@PrimaryKey(autoGenerate = true)`.

## Room database and DAO

`data/network/BooksDatabase.kt` declares `BooksDatabase` at schema version **5**, with `exportSchema = false`. Migrations are:

| Migration | SQL changes |
|---|---|
| `MIGRATION_2_3` | Adds non-null `genre TEXT` and `isbn TEXT`, each with default `''` |
| `MIGRATION_3_4` | Adds nullable `rating INTEGER` |
| `MIGRATION_4_5` | Adds nullable `notes`, `dateStarted`, `dateFinished`, and `language` text columns |

The three migrations are registered on the database builder in `AppModule.kt`.

`data/network/BooksDao.kt` exposes these operations:

| DAO method | Behavior |
|---|---|
| `getBooksSortedByTitle()` | `Flow<List<Book>>`, ordered by `title ASC` |
| `getBooksSortedByYear()` | `Flow<List<Book>>`, ordered by `year DESC` |
| `getBook(id)` | `Flow<Book?>` for the matching id |
| `addBook(book)` | Inserts one row with `OnConflictStrategy.IGNORE` |
| `insertAll(books)` | Inserts a list with `OnConflictStrategy.REPLACE` |
| `updateBook(book)` | Room `@Update` |
| `deleteBook(book)` | Room `@Delete` |

DAO methods are not declared `suspend`. List and detail reads return flows, which Room re-emits as observed rows change.

## BooksViewModel

`ui/home/HomeViewModel.kt` is the Hilt-injected state holder for the home list, overview, sorting, and backup operations.

### List and sorting

- `books` is Compose state containing the current `List<Book>`.
- `SortOption` has `TITLE` and `YEAR` values; persisted key `sort_option` stores the enum name in DataStore.
- During initialization, the ViewModel reads the first stored sort value, uses it if it matches an enum entry, then starts `getBooks()`.
- `getBooks()` selects the matching DAO flow, cancels the previously stored list collection job, and updates `books` for each emission.
- `updateSortOption()` updates the in-memory value, asynchronously saves its name, and refreshes the list collection.

### Detail and mutations

- `getBook(id)` cancels the previous detail collection, sets `bookDetail` to `Loading`, collects the DAO's `Flow<Book?>`, and sets it to `Loaded(book)` or `NotFound` for each result.
- `bookDetail` is a sealed state: `Loading`, `NotFound`, or `Loaded(Book)`.
- `getBooksSnapshot()` suspends until it receives the first list value from the DAO flow selected by the current sort option.
- `addBook`, `updateBook`, and `deleteBook` start ViewModel coroutines on `Dispatchers.IO` and call the corresponding DAO method. `insertAllBooks` is suspending and returns after the DAO insert completes on `Dispatchers.IO`.
- The separate `book` Compose state and `updateTitle`, `updateGenre`, `updateISBN`, `updateAuthor`, `updateYear`, and `updateStatus` helpers are also present. These helpers update that in-memory `Book` value; `updateYear` only changes the value when input parses as a `Long`, and `updateStatus` trims its input.

The home entry sheet builds a `Book` and calls `addBook`. The overview observes `bookDetail`, submits edited copies via `updateBook`, updates rating through the same path, and deletes via `deleteBook`.

## Settings DataStore

`ui/settings/SettingsViewModel.kt` declares the application preferences delegate:

```kotlin
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
```

The preference keys and defaults are:

| Key constant | Stored key | Type | Default when absent |
|---|---|---|---|
| `DARK_THEME_KEY` | `dark_theme` | Boolean | `false` |
| `SYSTEM_THEME` | `follow_system` | Boolean | `false` |
| `SORT_OPTION_KEY` (in `HomeViewModel.kt`) | `sort_option` | String | `SortOption.TITLE` in the ViewModel |

`SettingsViewModel` collects `DataStore.data` and exposes `hasUserDarkThemeEnabled` and `followSystemTheme` as Compose state. Its save methods write the corresponding boolean values through `DataStore.edit`. `NavGraph` uses those values with the system theme to select the app theme.

## JSON backup and restore

`ui/settings/backup/BackupScreen.kt` provides document-picker UI and the top-level helper functions used for JSON transfer. It uses Gson and the Android `ContentResolver`.

### Export

1. `createBackupIntent()` creates an `ACTION_CREATE_DOCUMENT` intent with MIME type `application/json` and a default title `bookCompanion_backup_yyyyMMddHHmmss.json` (timestamp formatted with the device locale).
2. After document selection, `performBackup(resolver, books, uri)` serializes the current `List<Book>` directly as a JSON array and writes UTF-8 bytes to the selected output stream.
3. It returns `ExportResult.Success` or `ExportResult.NoOutputStream`.

### Import

1. The screen launches `GetContent` with MIME type `application/json`.
2. `importBackupFile(viewModel, resolver, uri)` opens the input stream and reads at most 10 MiB plus one byte to determine whether the limit is exceeded. Files larger than 10 MiB return `ImportResult.FileTooLarge` without buffering the remainder.
3. Gson parses the JSON as an array of `Book`. A missing stream returns `EmptyStream`; a JSON `null` result or `JsonSyntaxException` returns `Malformed`.
4. Parsed entries are passed to `BooksViewModel.insertAllBooks`, which delegates to `BooksDao.insertAll` (`REPLACE` conflict behavior) and suspends until it completes. The helper returns `Success(count)` after completion or `RestoreFailed` if the database insert throws an exception. Coroutine cancellation is rethrown.

The result types are `ImportResult.Success(count)`, `EmptyStream`, `FileTooLarge`, `Malformed`, and `RestoreFailed`; export results are `ExportResult.Success` and `NoOutputStream`. The screen displays a localized toast for the result. Export obtains a first DAO-flow snapshot through `getBooksSnapshot()` before serializing the selected file.

## Android system backup configuration

The manifest enables Android backup and references `res/xml/backup_rules.xml` and `res/xml/data_extraction_rules.xml`. Those rules exclude the `book_table` database from full/cloud backup and device transfer. The data extraction rules also exclude `datastore/settings.preferences_pb` for cloud backup and device transfer. The app's explicit JSON backup flow is separate from these Android system backup rules.

## Backend-related tests

Unit tests are under `app/src/test/java/com/mukund/bookcompanion/`:

- `data/FakeBooksDao.kt` provides flow-backed fake DAO behavior and records mutation calls.
- `ui/home/BooksViewModel*Test.kt` covers list sorting and snapshots, detail flow state and replacement, CRUD delegation, field helpers, and year parsing.
- `ui/settings/SettingsViewModelTest.kt` covers persisted theme preference behavior.
- `ui/settings/backup/BackupFunctionsTest.kt` covers JSON import/export, malformed and oversized input (including bounded reads), restore write failures, empty data, and document intent properties.
- `util/MainDispatcherRule.kt` supplies a test main dispatcher for coroutine/ViewModel tests.

Run the unit test suite with `./gradlew test`.
