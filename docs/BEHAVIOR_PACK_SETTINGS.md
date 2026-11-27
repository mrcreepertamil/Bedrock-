# Behavior Pack — World Feature Settings

Bedrock++ uses **Custom Pack Settings** in the Behavior Pack manifest so a world owner can choose which feature groups are enabled for that world.

## Where players configure it

In Minecraft, open the world's Behavior Packs list, select the Bedrock++ Behavior Pack, and open its settings (gear) screen. The toggles are saved as world-level pack settings.

## Available switches

| Setting | Default | Intended feature group |
| --- | --- | --- |
| Cinematic Camera Tools | On | Camera and cinematic utilities |
| Replay Capture | Off | Future replay-data capture prototype |
| Replay Playback | Off | Future replay playback prototype |
| Actor Tools | Off | Future actor / machinima workflow tools |
| Scene Tools | Off | Future scene setup tools |

These are **configuration switches**, not completed features. The corresponding systems must be implemented and tested before any option is described as functional. In particular, Replay Capture and Replay Playback are not implemented by this settings scaffold.

## Code integration

`scripts/main.js` exports `getFeatureSettings()`. Feature modules should call it and initialize only the systems whose setting is enabled. Settings are read from `world.getPackSettings()`, and are scoped to the world.

## Compatibility note

Custom pack settings require manifest format version 3 and a minimum engine version of 1.21.130 or newer. The Script API exposes `world.getPackSettings()`; Microsoft currently documents the custom Behavior Pack settings workflow as experimental / subject to change. Test on the exact Bedrock release targeted by each build before distributing it.

## Current status

- [x] Add world-level toggle definitions to the Behavior Pack manifest.
- [x] Add a shared Script API settings reader.
- [ ] Connect each toggle to its actual feature implementation.
- [ ] Test the settings screen and saved values in Minecraft.
