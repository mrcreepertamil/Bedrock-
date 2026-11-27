# Bedrock++ Extensions for Android

This folder contains the Android companion app project for managing Bedrock++ extensions.

## Current status

This is the initial Android project scaffold. It builds an installable placeholder app; the extension browser, importer, updater, and Minecraft connection have not been implemented yet.

## Build locally

Requirements:
- JDK 17
- Android SDK with Android platform 35
- Gradle 8.9

From this directory:

```bash
gradle :app:assembleDebug
```

The APK is generated at:

```
app/build/outputs/apk/debug/app-debug.apk
```

## GitHub Actions releases

The workflow at `.github/workflows/extensions-apk-release.yml` builds an installable debug APK and attaches it to a GitHub Release.

Create and push a version tag:

```bash
git tag extensions-v0.1.0
git push origin extensions-v0.1.0
```

Or run **Build and Release Bedrock++ Extensions APK** manually from the GitHub Actions tab and enter a version such as `0.1.0`.

> The first workflow publishes a debug-signed APK for testing. Before distributing production builds, configure a private Android release keystore using GitHub Actions secrets and add a proper release-signing step. Never commit a keystore or its passwords.
