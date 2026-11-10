# Anime Great Sage v1.2 — Raphael Hybrid AI foundation

**Project:** Project Raphael  
**App:** Anime Great Sage  
**Assistant:** Raphael, the Great Sage  
**Tagline:** Your Intelligence. Your World. Your Great Sage.

This source tree is based on the supplied v1.1 Android project. The v1.1 archive is not modified.

## What is included in this v1.2 source foundation

- Preserved Kotlin + Jetpack Compose app shell, guest profile and beginner tour.
- App version metadata updated to 1.2.0.
- Device capability inspection for Android version, ARM64 ABI, RAM and free storage.
- AI provider interfaces and consent-aware provider routing.
- Structured knowledge schemas for anime, manga, games, study, culture, cooking, energy traditions and general knowledge.
- Timeline data structures for original release, in-universe chronology and recommended first-time order.
- Game journey data structures for release/story sequence, campaigns and expansions.
- Anime/game progress record model.
- Voice-conversion provider interface and voice profile model.
- Default-off consent policy model.
- GitHub Actions workflow to build a debug APK.

## Important implementation status

This is a **source-code foundation**, not a finished AI assistant.

Not yet implemented or verified:
- No local GGUF model or llama.cpp/llama.kt inference engine is bundled.
- No online AI backend is configured; no conversation data is sent online.
- No working voice conversion, voice cloning, microphone capture or call-audio routing.
- No populated/exhaustive anime, game, recipe, study or energy database.
- No live web catalog, release tracker or source-verification service.
- Progress data models exist, but progress persistence and tracker UI are not complete.
- No wallpaper picker or wallpaper-setting implementation is included in this increment.

The app must not label these unfinished modules as working features. A compatible-device check is only a preliminary screening and does not guarantee acceptable model performance.

## Compatibility

- Android minimum: API 23 (Android 6.0), retained from v1.1.
- Target/compile SDK: 35.
- Java/JVM target: 17.
- Planned local inference requires a separate runtime integration and device testing. The preliminary capability checker looks for Android 10+, ARM64, sufficient available RAM and free storage.

## Privacy and permissions

- Guest profile remains local to the device through DataStore.
- No `INTERNET` or `RECORD_AUDIO` permission is added in this foundation.
- No automatic browsing, background microphone use, unsolicited notifications, payments or banking features.
- Online AI, account connection and microphone use must remain disabled until implemented with clear, explicit user consent.

## Build

The included GitHub Actions workflow uses JDK 17 and Gradle 8.11.1:

```bash
gradle :app:assembleDebug --no-daemon
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

## Versioning

- v1.1: preserved original project supplied by the user.
- v1.2.0: hybrid-AI architecture and feature-model foundation.
