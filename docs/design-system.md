# Design System

The design system lives in `app/src/main/java/com/mukund/bookcompanion/design/`. It is a warm editorial aesthetic — Monocle / Kinfolk-inspired — with high contrast between light and dark modes.

## Color

### Palette (`DesignTokens.kt` → `BookCompanionColors`)

Raw color values. Never reference these directly in Composables.

**Light**

| Token | Hex | Role |
|---|---|---|
| `paper` | `#F2EADB` | Primary background |
| `paperDeep` | `#EBE1CE` | Elevated surfaces, cards |
| `ink` | `#1C1814` | Primary text |
| `inkSoft` | `#4A3F36` | Secondary text, icons |
| `inkFaint` | `#6F6152` | Secondary labels, placeholders |
| `rule` | `#D8CBB2` | Dividers, borders |
| `ruleSoft` | `#E5DCC7` | Subtle separators |
| `terracotta` | `#B8543E` | Accent — icons, indicators, highlights |
| `terracottaText` | `#A14432` | Accessible terracotta text |
| `sage` | `#6B7F5A` | Accent — positive states |
| `ochre` | `#B8893E` | Accent — warnings, emphasis |

**Dark**

| Token | Hex | Role |
|---|---|---|
| `paper` | `#1A1410` | Primary background |
| `paperDeep` | `#2A2420` | Elevated surfaces, cards |
| `ink` | `#E8DCC8` | Primary text |
| `inkSoft` | `#B8A894` | Secondary text, icons |
| `inkFaint` | `#A79A8A` | Secondary labels, placeholders |
| `rule` | `#3D3530` | Dividers, borders |
| `ruleSoft` | `#2E2924` | Subtle separators |
| `terracottaText` | `#D27A64` | Accessible terracotta text |

`terracotta`, `sage`, and `ochre` are shared across themes for accents and non-text indicators. Use `terracottaText` for text to retain contrast in both modes.

### Scheme (`BookCompanionColorScheme`)

A resolved color set for the current mode. Two instances: `LightColorScheme`, `DarkColorScheme`. These map the semantic names above to the appropriate palette values.

### CompositionLocal

```kotlin
val LocalBookCompanionColors = compositionLocalOf { LightColorScheme }
```

`Theme.kt` provides the correct scheme based on `darkTheme`. In any Composable:

```kotlin
val colors = LocalBookCompanionColors.current
// e.g.: colors.paper, colors.ink, colors.terracotta
```

**Shorthand:** `ui/theme/Color.kt` exposes a `bookColors` property that calls `LocalBookCompanionColors.current`. Screens use `bookColors.paper` directly.

### Rules
- Always use semantic tokens (`ink`, `paper`, `rule`) — not raw hex.
- Accents (`terracotta`, `sage`, `ochre`) are the same in light and dark; use them for icons and status indicators. Use `terracottaText` for terracotta-colored text.
- `paperDeep` is for elevated surfaces (cards, bottom sheet interiors) — not for full-screen backgrounds.

---

## Typography

### Font families (`ComposeFonts.kt`)

| Family | Weights | Role |
|---|---|---|
| `CormorantGaramond` | 400, 400-italic, 500, 500-italic | Display text, headings, large body copy |
| `IBMPlexSans` | 400, 500 | UI labels, captions, utility text |
| `JetBrainsMono` | 400 | Metadata, counts, numeric values, technical labels |

Font files live in `app/src/main/res/font/`.

### TextStyle constants (`ui/theme/Type.kt` → `AppType`)

`AppType` is the single object to import in Composables. Do not construct `TextStyle` objects inline in UI code.

| Token | Family | Use case |
|---|---|---|
| `AppType.displaySerif` | CormorantGaramond | Screen titles, hero text |
| `AppType.displaySerifItalic` | CormorantGaramond italic | Section headers (e.g., "Backup & Restore") |
| `AppType.bodySerif` | CormorantGaramond | Book titles in cards |
| `AppType.bodySmall` | IBMPlexSans | Navigation labels, back links |
| `AppType.label` | IBMPlexSans | Form labels, captions |
| `AppType.mono` | JetBrainsMono | Year values, ISBNs, counts |

Usage:

```kotlin
Text(text = "My Books", style = AppType.displaySerif, color = bookColors.ink)
```

---

## Spacing (`BookCompanionSpacing`)

8dp base unit with 4dp half-step increments.

| Token | Value | Use |
|---|---|---|
| `xs` | 4dp | Tight internal gaps |
| `sm` | 8dp | Between sibling elements |
| `md` | 12dp | Default gap |
| `lg` | 16dp | Larger gaps, list item padding |
| `xl` | 24dp | Section separators |
| `xxl` | 32dp | Major section padding |

Padding and margin presets (`paddingSmall` → `paddingXLarge`, `marginSmall` → `marginXLarge`) cover common use cases. Use these before reaching for hardcoded `dp` values.

---

## Shape (`BookCompanionRadius`)

| Token | Value | Use |
|---|---|---|
| `small` | 4dp | Small interactive elements |
| `medium` | 8dp | Default — most components |
| `large` | 12dp | Cards, larger surfaces |
| `xlarge` | 20dp | Bottom sheets, modals |
| `pill` | 999dp | Fully rounded buttons, chips |

---

## Borders (`BookCompanionBorders`)

| Token | Value | Use |
|---|---|---|
| `hairline` | 0.5dp | Default dividers (most `HorizontalDivider` calls) |
| `emphasis` | 1dp | Accent borders |
| `thick` | 2dp | Active/highlighted states |

---

## Opacity (`BookCompanionOpacity`)

| Token | Value | Use |
|---|---|---|
| `full` | 1.0 | Normal |
| `strong` | 0.87 | Primary text on colored backgrounds |
| `medium` | 0.60 | Secondary elements |
| `soft` | 0.38 | Disabled / placeholder (same as M3 disabled alpha) |
| `faint` | 0.12 | Overlay tints |

---

## Animation timing (`BookCompanionTiming`)

| Token | Value | Use |
|---|---|---|
| `fast` | 200ms | Micro-interactions (button press, focus) |
| `medium` | 300ms | Standard transitions |
| `slow` | 400ms | Deliberate animations (sheet slide) |
| `slower` | 500ms | Emphasis / page transitions |

Screen-to-screen nav transitions are defined separately in `navigation/AnimationDefaults.kt`.

---

## What not to do

- Do not add hardcoded `Color(0xFF...)` values in Composable files — add a token to `DesignTokens.kt` instead.
- Do not use `MaterialTheme.colorScheme.*` for app colors — the design uses `LocalBookCompanionColors`, not the M3 color system directly.
- Do not use `MaterialTheme.typography.*` for text styles — use `AppType.*`.
- Do not add new font files without adding the corresponding `FontFamily` entry in `ComposeFonts.kt` and `TextStyle` entries in `AppType`.
