# Bedrock++

**A private, evolving enhancement project for Minecraft Bedrock Edition.**

Bedrock++ aims to improve and expand the Bedrock experience while keeping the identity and feel of vanilla Minecraft.

## Project direction

- Improve and rethink parts of the Bedrock user experience and interface.
- Explore cinematic creation tools inspired by Java Edition projects such as Blockbuster and Replay Mod.
- Investigate replay capture, playback, camera paths, scene tools, and actor workflows.
- Build structure capture, schematic, and guided-building tools.
- Use the Bedrock++ companion APK for supported file-management and conversion workflows.
- Keep features modular where practical and validate platform constraints before promising functionality.

## Current prototypes

- Behavior Pack structure capture can save a selected block region into the current world's structure storage.
- The Android companion has an initial converter that turns a Bedrock++ structure-data JSON file into a Bedrock .mcstructure file.
- The add-on-to-APK transfer bridge is not implemented yet, so automatic capture-to-file export is not complete.
- The custom Structure Block UI button is still under investigation; a visible JSON UI button is not proof that its click can call add-on JavaScript.

See the [roadmap](docs/ROADMAP.md), [feature notes](docs/FEATURES.md), [structure export notes](docs/STRUCTURE_EXPORT.md), and [interchange format](docs/STRUCTURE_INTERCHANGE_FORMAT.md).

## Design principles

1. **Vanilla first** — enhancements should respect Minecraft's visual language and identity.
2. **Useful over excessive** — every feature should solve a real problem or add meaningful creative possibilities.
3. **Modular** — features should be separable where practical.
4. **Feasibility before promises** — Bedrock add-ons have different technical access from Java mods. Each feature must be prototyped against supported Bedrock capabilities before it is promised.
5. **No mob behaviour overhaul** — changing mob AI or behaviour is outside the current project direction.

## Project access and ownership

This is a private, proprietary project. Access to the repository does not grant permission to redistribute, publish, modify, or reuse the project outside the terms explicitly authorized by its owner. See [LICENSE](LICENSE).

Minecraft is a trademark of Microsoft/Mojang. This is an independent, unofficial project and is not approved by or affiliated with Mojang or Microsoft.
