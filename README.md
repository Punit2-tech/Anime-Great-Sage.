# Anime Great Sage v1.1 — Android starter project

Project/development name: Project Raphael  
App display name: Anime Great Sage  
Assistant identity: Raphael, the Great Sage

## What is implemented in this starter
- Native Android app shell using Kotlin and Jetpack Compose.
- First-run guest profile with optional name and nickname.
- Beginner onboarding tour with skip and replay.
- Home, Explore, and Profile navigation.
- Blue cosmic-orb visual made from native Compose gradients (not an imported AI image).
- Local guest display-name and tutorial-state storage using DataStore.
- No analytics, advertising SDK, account sign-in, network permission, or cloud upload is included.

## What is NOT implemented yet
This is a source-code foundation, not the complete promised product and not a built APK. The following require separate implementation, service selection, testing, and often licensed data/API access:
- Live AI chat and model backend.
- Complete, maintained anime/manga/manhwa catalog and source attribution.
- Search, title matching, recommendations, timelines and watch/read orders.
- Franchise-specific race, magic, skill and power encyclopedias.
- Real-time voice conversations and voice changing (Android/device restrictions apply).
- Wallpaper picker and system wallpaper confirmation flow.
- User account connection/sync, moderation, backup and account deletion.
- Official-media retrieval, authorized embeds, fan-art opt-in and copyright checks.
- Production security review, accessibility, localization, crash testing and release signing.

The interface labels these features as pending rather than pretending they work.

## Build in Android Studio
1. Install Android Studio with a JDK 17-compatible setup and Android SDK Platform 35.
2. Open this folder (`AnimeGreatSage-Android-v1.1`) in Android Studio.
3. Allow Gradle sync to finish.
4. Use **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. For a distributable release, configure your own secure signing key under **Build > Generate Signed Bundle / APK**.
6. Test the signed APK on multiple real Android devices before sharing it.

The generated debug APK is normally at:
`app/build/outputs/apk/debug/app-debug.apk`

A signed release APK is normally at:
`app/build/outputs/apk/release/app-release.apk`

## Compatibility
- `minSdk = 23` is a provisional broad-compatibility starting point (Android 6.0+), not a guarantee that every planned feature will work on every device.
- `targetSdk = 35`, `compileSdk = 35`, Java 17.
- The final minimum Android version must be validated against all selected libraries and planned features before release.

## Important release/security notes
- Keep the Android signing key private and backed up securely. Do not send it in WhatsApp or include it in the source ZIP.
- Never distribute an unsigned or unverified production build.
- Before collecting any personal data or adding cloud AI services, publish a clear privacy notice, minimize data collection, secure transport/storage, and obtain consent for optional sharing.
- For WhatsApp distribution, share the signed APK only after testing it. Some messaging apps impose file-size/type restrictions; an official download link is a useful alternative.
- The website should retain only the three requested sections: Home, Description, Download.

## Project identity
Package/application ID: `com.projectraphael.animegreatsage`  
Version name: `1.1`  
Version code: `11`
