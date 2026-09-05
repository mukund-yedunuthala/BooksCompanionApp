# Changelog

### [1.0.2](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/1.0.1...1.0.2) (2026-09-05)

### [1.0.1](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/1.0.0...1.0.1) (2026-06-16)

## [1.0.0](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/0.2.2...1.0.0) (2026-06-16)

### Features

* **overview:** load/not-found state, modal delete dialog, accessible rating
(+ i18n, tokens)
([571c4b0](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/571c4b0c3668ca58ad678868e3b78ab8794ce717))
* **home:** list, validation, and a11y fixes (+ strings, tokens)
([0c22743](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/0c227434c99a2677b872f40264786bc0ea174fd7))
* display notes, language, and reading dates in OverviewContent
([ce0d3b3](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/ce0d3b3552696ce2b7efa917c3cceabf7ec040bd))
* add notes, language, and dates to BookAdditionBottomSheet
([31bdeae](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/31bdeaed73f8f9efc38c4afc837d0ef8ca344a72))
* add notes/language/dates fields to Book entity and migrate DB to v5
([66e589a](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/66e589a8c5467ae7546702444b46165b7219b8d3))
* persist sort option across sessions via DataStore
([8ae68f9](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/8ae68f92151025f0a37987b0ada71b29754fc560))
* wire SortOption through DAO/repository/ViewModel and add sort button to home
top bar
([ef8a1ef](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/ef8a1ef32df6e2ef0d8376dec071d547ce525366))
* add rating field to Book, DB migration to v4, and clickable star row in
OverviewContent
([c92299e](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/c92299ebd1288336c836ac5da666c4e4bd7a0c83))
* add Reading category to BookCategory and wire filter tab through home
pipeline
([fbf8925](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/fbf89257136391f4325e25f08486288811b61ae2))

### Fixes

* **backup:** return import/export result types and show honest toasts
([4f22c33](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/4f22c33568da97621b1d5f239c7ade36d823adc7))
* **settings:** 48dp back targets, gate Role.Button to actionable rows (+
i18n, tokens)
([07c7f2d](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/07c7f2d30edc277b662a5b850e435cdc7c59d948))
* **theme:** drive Material3 light & dark from the editorial palette
([8aba99b](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/8aba99bf867b7756923a609a9787e47e923fd9de))
* sort label, next-option contentDescription, and statusLabel filter (DL-4)
([974c829](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/974c82967ea389c77bab064595b033db3ecb6979))
* suppress arrow and clickable on App Version row via showArrow param (BF-3)
([60206b9](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/60206b9cf343ee2a3110fdfe549173adab557c42))
* exclude DB and DataStore from backup; guard importBackupFile
OOM/JsonSyntaxException; wire proguard-rules.pro
([07d1521](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/07d15211b290b47dd032ad45a070afadda6b00b5))
* run backup export I/O on a background coroutine
([1ae9152](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/1ae9152d24c0e921de14be3e6a44426a3b136ee7))
* resolve P2 bugs found in functionality audit
([92dce89](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/92dce8948a253985a79e4d69a3f72a6a6b3086d8))

### [0.2.2](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/0.2.1...0.2.2) (2026-06-05)

#### Features

* apply design system to settings screen
([944adae](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/944adae8ea1c3388f609046ef78a38a15df03f55))
* apply design system to overview screen
([0f1d981](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/0f1d9812df1ded8f9039a62a8980e262cc0570ba))
* apply design system to home screen
([9efd3d9](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/9efd3d9a488a5f0fd9732e7a622768cbc27f2e9c))
* add editorial design system and wire into theme
([ba3b853](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/ba3b853778283402adafd23d0d2a78386c4b958b))

### [0.2.1](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/0.2.0...0.2.1) (2026-03-18)

## [0.2.0](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/0.1.5...0.2.0) (2025-09-30)

### Features

* add bottom sheet and new monochrome icon
([504b7f5](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/504b7f593094c3add62534ec466179785b143858))

### [0.1.5](https://github.com/mukund-yedunuthala/BooksCompanionApp/compare/0.1.4...0.1.5) (2025-02-11)

### 0.1.4 (2024-11-21)

#### Features

* add monochrome icon support
([d7560f6](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/d7560f67814c76e07f9bc6bc7c0a674739d304e8))
* add genre and isbn fields
([84374c7](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/84374c77e39055698e561b784c6e107fd7fec05c)),
closes
[#16](https://github.com/mukund-yedunuthala/BooksCompanionApp/issues/16)
* support app level language setting (on Android 13+)
([0c2ea0a](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/0c2ea0ac6a84f0175c740ef47bbac2683af61b87))

#### Fixes

* backup bug fix
([e669b92](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/e669b926348b7b7c5415e360cccb28f316a939a2))
* crashes on SDK<32
([73f006b](https://github.com/mukund-yedunuthala/BooksCompanionApp/commit/73f006bf4d83da54dd90960da8db1d6908b61d0b))
