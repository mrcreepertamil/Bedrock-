# Structure Export

## Current in-game capture prototype

Structure Export is a built-in Bedrock++ Behavior Pack feature. It uses the stable Script API's world.structureManager.createFromWorld() method to capture a cuboid region and save it to the current world's structure storage.

### In-game workflow

1. Enable **Structure Export** in the Bedrock++ Behavior Pack's world settings (gear icon).
2. Enter the world and hold a normal stick.
3. Interact with the block at the first corner of the region.
4. Interact with the block at the opposite corner.
5. Enter a structure name in the form.
6. Bedrock++ saves it under the identifier bedrockpp:<name> in the world's structure storage.

The selection is per-player and per-dimension. The prototype captures blocks only; entities are excluded. The region is limited to 64 blocks on each axis and 32,768 blocks total.

## Companion APK conversion

The Android companion now has a first conversion prototype. It accepts a dedicated Bedrock++ structure-data JSON file and creates an uncompressed, little-endian .mcstructure file using format version 1. The interchange schema is documented in STRUCTURE_INTERCHANGE_FORMAT.md.

The converter currently supports block palettes and block indices only. Entities, block entities, scheduled ticks, and auxiliary block data are not preserved.

## Important integration limitation

The current in-game capture prototype saves a structure **inside the Minecraft world**. It does not produce a standalone interchange JSON file in Android storage. The stable Script API does not provide a general-purpose API for writing arbitrary files to Android storage, and the custom Structure Block UI button has not been proven to call Behavior Pack JavaScript.

Therefore, the APK conversion stage and the Minecraft capture stage exist as separate prototypes; the complete automatic capture-to-APK workflow is **not yet connected**. Do not claim that selecting the custom Structure Block button already creates a file. A supported transfer bridge must be designed and tested before end-to-end export can be called complete.

## Testing still required

- Build and install the Android companion APK.
- Validate converter output with real Bedrock-generated .mcstructure fixtures and a Bedrock import test.
- Test different palette states, negative coordinates, and maximum supported dimensions.
- Implement and test the add-on-to-companion transfer producer.
- Verify that the custom Structure Block UI button can invoke a supported action; JSON UI custom button events do not automatically call Script API code.
- Verify the world-storage capture and persistence workflow in Minecraft on the target Android release.
