# Bedrock++ Companion

The Android companion is built with **Flutter and Dart**. Its native Android layer handles Android's document picker and bridges the Flutter interface to the existing Bedrock structure NBT converter.

## Current interface

- Purple/dark Bedrock++ dashboard.
- Structure converter workspace.
- Import a .bppstruct.json interchange file.
- View the selected structure's name, dimensions, block count, and palette size.
- Save the selected data as a .mcstructure file through Android's save dialog.
- Import and export progress, error, and success states.

## Structure conversion

1. Tap **Import structure data** and choose a Bedrock++ JSON interchange file.
2. The app validates its dimensions, palette, and block indices.
3. Review the structure summary.
4. Tap **Create .mcstructure file** and choose where Android should save it.

The converter writes an uncompressed, little-endian Bedrock .mcstructure file using format version 1.

## Current scope and limitations

- Interchange format: bedrockpp-structure, version 1.
- Up to 64 blocks per axis and 32,768 blocks total.
- Block palette and primary block layer are supported.
- Secondary layer is optional and defaults to all void.
- Entities, block entities, scheduled ticks, and auxiliary data are not included.
- The in-game add-on still does not automatically transfer captured structures into the APK. The app currently imports a user-selected interchange file; the end-to-end capture-to-APK bridge remains future work.

See [the interchange format](../docs/STRUCTURE_INTERCHANGE_FORMAT.md) and [structure export notes](../docs/STRUCTURE_EXPORT.md).

## Build locally

Requirements:
- Flutter 3.29.3 (stable) or a compatible newer stable version
- Dart SDK supplied by Flutter
- JDK 17
- Android SDK

From this directory:

    flutter pub get
    cd android
    gradle wrapper --gradle-version 8.10.2
    cd ..
    flutter build apk --debug

The APK is generated at:

    build/app/outputs/flutter-apk/app-debug.apk

The GitHub Actions workflow builds a debug APK for testing. A production release needs a private signing key configured as repository secrets; never commit a keystore or its passwords.
