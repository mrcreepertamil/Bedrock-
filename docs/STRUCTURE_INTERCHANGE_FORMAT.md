# Bedrock++ Structure Interchange Format

The companion app accepts a UTF-8 JSON document with this schema:

    {
      "format": "bedrockpp-structure",
      "version": 1,
      "name": "example_build",
      "size": [2, 1, 2],
      "origin": [100, 64, -20],
      "palette": [
        {"name": "minecraft:stone", "states": {}, "version": 18168865},
        {"name": "minecraft:oak_log", "states": {"pillar_axis": "y"}, "version": 18168865}
      ],
      "blocks": [0, 1, 1, 0],
      "secondary": [-1, -1, -1, -1]
    }

## Fields

- format: must be bedrockpp-structure.
- version: currently 1.
- name: suggested output filename; unsafe filename characters are replaced.
- size: positive X, Y, Z dimensions; each axis is at most 64.
- origin: three integer world coordinates.
- palette: block permutation entries. Each entry has a Bedrock block name, optional states, and optional block version.
- blocks: primary-layer palette indices, one per block.
- secondary: optional secondary-layer indices; omitted means all -1 (void).

Block indices are ordered with Z changing fastest, then Y, then X. A value of -1 means void. State values may be strings, integers, or booleans. Boolean states are encoded as NBT bytes.

## Converter output

The Android companion writes an uncompressed, little-endian NBT .mcstructure using format_version 1. It includes the size, both block-index layers, a single default palette, empty entity and block-position-data containers, and the world origin.

## Scope

Version 1 is deliberately block-only. It does not preserve entities, block entities (such as container contents), scheduled ticks, or arbitrary world metadata. Unsupported or malformed JSON is rejected instead of silently guessed.

The add-on-to-APK transfer producer is not implemented yet. The current Minecraft Script API prototype saves structures into world structure storage; it cannot write arbitrary files to Android storage. A later, separately tested transfer bridge must produce this JSON format before the APK can convert a captured build automatically.
