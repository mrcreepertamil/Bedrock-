# UI Pack Research: Déesse UI v1.3.96

## Purpose

This document records observations from the locally supplied `Desse-UI-v1.3.96.mcpack`. The pack is a reference for understanding the scope and organization of a substantial Bedrock UI resource pack. Bedrock++ will create its own implementation and visual identity.

## Package facts

- Pack name: Déesse UI Pack
- Version: 1.3.96 (build 218)
- Manifest format: 2
- Module type: `resources`
- Manifest minimum engine version: `[1, 21, 100]`
- The pack description and changelog state compatibility targeting Minecraft 26.60+.
- The manifest declares three subpacks: Default, Full Features, and Performance.
- The archive contains 570 entries: 204 files under the main `ui/` tree, 334 entries under `textures/`, plus language, sound, material, manifest, and subpack content.
- The main UI definitions are JSON files. The archive contains no JavaScript, TypeScript, or HTML source files.

## UI areas present in the reference

The main `ui/_ui_defs.json` registers definitions covering:

- Start screen and Play/server screens
- Settings and settings-related screens
- Pause screen and HUD
- Inventory, gameplay, chat, and progress screens
- Furnace, brewing, enchanting, anvil, stonecutter, trade, and book screens
- Custom Déesse screens, tools, and quick-container interfaces

The pack also contains a dedicated `textures/déesse_ui/ore_ui/` texture collection, including button, dialog, miscellaneous, and screen assets. This indicates an Ore-UI-inspired visual treatment within a resource-pack UI implementation; it does not make the pack itself the native Ore UI application or React Facet source.

## Useful design lessons for Bedrock++

1. Treat the UI as a system spanning many screens, not just a main-menu reskin.
2. Keep shared components such as buttons, toggles, dialogs, dropdowns, and sliders consistent.
3. Separate screen definitions from reusable controls and visual assets.
4. Plan for different input methods and screen sizes.
5. Keep optional feature groups modular; the reference uses subpacks for different feature configurations.
6. Maintain compatibility notes and test after Minecraft updates.

## Important boundary

The supplied pack's own source comments explicitly reserve its code and prohibit reuse without permission. Bedrock++ will not copy its JSON definitions, textures, or other authored assets. We will use only high-level observations about organization and feature scope, then write original UI definitions and create original assets.

## Bedrock++ implementation direction

- Investigate current Bedrock JSON UI capabilities and limitations before implementing each screen.
- Keep native Ore UI / React Facet research separate from resource-pack JSON UI implementation.
- Do not promise that a resource pack can replace native screens that Mojang does not expose for supported customization.
- Start with an original, small screen prototype and validate it in the target Minecraft version before expanding.
