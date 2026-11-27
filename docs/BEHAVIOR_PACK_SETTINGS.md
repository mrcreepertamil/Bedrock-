# Behavior Pack — World Feature Settings

Bedrock++ uses custom Behavior Pack settings so a world owner can choose which feature groups are enabled for that world.

## Where players configure it

Open the world's **Behavior Packs** list, select the Bedrock++ Behavior Pack, and open its **Settings (gear)** screen. Configure the toggles before entering the world.

## Available switches

| Setting | Default | Intended feature group |
| --- | --- | --- |
| Cinematic Camera Tools | On | Camera and cinematic utilities |
| Replay Capture | Off | Future replay-data capture prototype |
| Replay Playback | Off | Future replay playback prototype |
| Actor Tools | Off | Future actor / machinima workflow tools |
| Scene Tools | Off | Future scene setup tools |
| Structure Export | Off | Capture a selected region into this world's structure storage |

Replay Capture, Replay Playback, Actor Tools, Scene Tools, and Cinematic Camera Tools still need their actual feature implementations. Structure Export has an initial Script API prototype; it has not yet been tested in Minecraft.

## Feature gating

- `scripts/feature_manager.js` reads the world settings.
- Feature modules register an initializer with `registerFeature(settingKey, initializer)`.
- The entry point calls `initializeEnabledFeatures()`.
- An initializer is called only when its matching setting is exactly `true`.
- Disabled or unregistered features are not started.

Example for a future feature module:

```js
import { registerFeature } from "../feature_manager.js";

registerFeature("bedrockpp:cinematic_camera", async ({ world, settings }) => {
  // Initialize the cinematic camera system here.
});
```

When a feature implementation is added, import its module in `scripts/main.js` before the startup call.

## Compatibility

Custom pack settings require manifest format version 3 and a minimum engine version of 1.21.130 or newer. The Script API exposes `world.getPackSettings()`. Microsoft's custom Behavior Pack settings workflow is documented as experimental / subject to change, so test on the exact Bedrock release targeted by each build.

Structure Export additionally depends on `@minecraft/server-ui` 2.0.0 for its naming form.

## Status

- [x] Add world-level toggle definitions to the Behavior Pack manifest.
- [x] Add a shared Script API settings reader.
- [x] Add a feature initializer registry and toggle gate.
- [x] Add the first Structure Export implementation and module registration.
- [ ] Test the settings screen and saved values in Minecraft.
- [ ] Test Structure Export on the target Android and Bedrock versions.
