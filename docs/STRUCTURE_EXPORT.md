# Structure Export

## Current prototype

Structure Export is a built-in Bedrock++ Behavior Pack feature. It uses the stable Script API's `world.structureManager.createFromWorld()` method to capture a cuboid region and save it to the current world's structure storage.

### In-game workflow

1. Enable **Structure Export** in the Bedrock++ Behavior Pack's world settings (gear icon).
2. Enter the world and hold a normal stick.
3. Interact with the block at the first corner of the region.
4. Interact with the block at the opposite corner.
5. Enter a structure name in the form.
6. Bedrock++ saves it under the identifier `bedrockpp:<name>` in the world's structure storage.

The selection is per-player and per-dimension. The prototype captures blocks only; entities are excluded. The region is limited to 64 blocks on each axis and 32,768 blocks total to keep the first implementation bounded.

## Important file-export limitation

This prototype saves a structure **inside the Minecraft world**. It does not create a standalone `.mcstructure` file in Android Downloads or another device folder. The stable Script API exposes world structure capture and storage, but it does not provide a general-purpose API for writing arbitrary files to Android storage.

The companion APK's file-export workflow therefore remains a separate integration task. We need to prove a supported APK-to-Minecraft data-transfer path before claiming that this feature can export a portable `.mcstructure` file on Android.

## Technical implementation

- Script API: `world.structureManager.createFromWorld()`
- Save mode: `StructureSaveMode.World` (persists with the world)
- UI: `@minecraft/server-ui` `ModalFormData` for naming
- Selection: two block interactions while holding a stick
- No `/structure save`, `/fill`, or `/setblock` command is used by the implementation.

## Testing still required

- Verify the Behavior Pack setting appears and persists on the target Bedrock release.
- Verify both corner selections on touch and controller input.
- Verify the name form opens and handles cancel/invalid/duplicate names.
- Verify saved structures survive leaving and reopening the world.
- Measure performance and test the size cap on lower-end Android devices.
- Prototype the companion APK transfer path before adding portable file export.
