# Bedrock++ Companion for Android

The companion app includes the first structure-file conversion workflow.

## Structure conversion

1. Choose a Bedrock++ structure-data JSON file (.bppstruct.json).
2. The app validates its dimensions, palette, and block indices.
3. Choose **Create .mcstructure file**.
4. Pick a destination using Android's system document picker.

The app writes an uncompressed, little-endian Bedrock .mcstructure file using format version 1.

## Current scope and limitations

- Interchange format: bedrockpp-structure, version 1.
- Up to 64 blocks per axis and 32,768 blocks total.
- Block palette and primary block layer are supported.
- Secondary layer is optional and defaults to all void.
- Entities, block entities, scheduled ticks, and other auxiliary data are not included in this first converter.
- This app currently converts an interchange file supplied by the user. The in-game add-on does not yet have a supported automatic file-transfer API to create that file directly on Android. Do not treat the end-to-end capture-to-APK workflow as complete until that bridge is implemented and tested.

See docs/STRUCTURE_INTERCHANGE_FORMAT.md for the file schema and docs/STRUCTURE_EXPORT.md for the add-on capture limitations.

## Build locally

Requirements:
- JDK 17
- Android SDK with Android platform 35
- Gradle 8.9

From this directory:

    gradle :app:assembleDebug

The APK is generated at:

    app/build/outputs/apk/debug/app-debug.apk

The GitHub Actions workflow builds a debug APK for testing. A production release needs a private signing key configured as repository secrets; never commit a keystore or its passwords.
