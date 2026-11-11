# Anime Great Sage v1.3.0 — Project Raphael

**Project:** Project Raphael  
**App:** Anime Great Sage  
**Assistant:** Raphael, the Great Sage  
**Tagline:** Your Intelligence. Your World. Your Great Sage.  
**Origin:** Made in Andhra Pradesh, India

Anime Great Sage is an Android app project built with Kotlin and Jetpack Compose. Version 1.3.0 is being developed on the `v1.3-development` branch as the next increment after the v1.2 foundation.

## v1.3 development status

**Status: In development — not yet build-verified.**

The current v1.3 source includes:
- The Android app shell with Home, Explore and Profile navigation.
- Guest onboarding and a beginner welcome tour.
- Local guest display-name storage.
- Optional nickname editing in Profile → Settings → General.
- A Compose-based Raphael orb and feature overview cards.
- Version metadata set to 1.3.0.

The current screens are a foundation. A card or screen description does not mean the underlying service is implemented or operational.

## Planned v1.3 work

The v1.3 roadmap includes:
- A secure guest-account foundation, with optional account/password and secure backup or sync only after the required authentication and security design is implemented.
- A secure Owner Mode with authenticated access and a return to normal guest mode.
- Real Raphael AI integration, selected and tested before being described as operational. Local AI and optional online fallback require clear user consent.
- Context-aware spelling and transcription correction, with clarification when meaning is uncertain.
- Context-aware, occasional anime-inspired humour.
- Home-screen Quick Access shortcuts and natural-language text/voice navigation to available in-app screens, once the required assistant and voice components are operational.
- A composed, original Raphael-inspired voice experience, subject to suitable authorization and provider integration.
- User-controlled sharing through Android's supported sharing flow, including Instagram where available.

These are planned requirements, not claims that the features are already implemented. No paid AI or voice service is to be enabled without separate user approval.

## Anime Music Library

The Anime Music Library is planned for **v1.3.5**, after v1.3 has been completed, built and tested. It is not part of the current v1.3 implementation.

## Compatibility and build

- Minimum Android version retained from the existing project: API 23 (Android 6.0).
- Compile/target SDK and Java settings are defined in the Gradle project files.
- Compatibility and performance must be verified through actual builds and device testing; they are not guaranteed by configuration alone.

The Android debug APK is intended to be built with the repository's GitHub Actions workflow. A successful v1.2 build is not evidence that v1.3 builds successfully. The v1.3 build must be run and its result checked separately.

## Privacy, safety and permissions

- Guest access remains an important app requirement; registration must not be forced for ordinary use.
- Personal data should be minimized and protected, with clear consent and user control.
- Microphone access, online AI, account connections and any optional cloud sync must remain disabled until implemented with clear user consent and appropriate safeguards.
- No hidden microphone use, secret recording, unsolicited background speech, automatic website interactions, payments or banking features.
- Third-party content, APIs, datasets, media and voices must be used only with appropriate rights, permissions and attribution.

## Project branches

- `main` — preserved v1.1 branch; do not use for v1.3 development.
- `v1.2-hybrid-ai` — preserved v1.2 branch; do not use for v1.3 development.
- `v1.3-development` — active branch for v1.3 work.

Keep v1.3 changes on `v1.3-development`. Preserve the tested v1.3 baseline before starting the separate v1.3.5 increment.

## Build command

```bash
gradle :app:assembleDebug --no-daemon
```

Expected debug APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

A successful build confirms compilation and packaging only. Individual app features still require functional testing on supported devices.
