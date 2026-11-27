# Bedrock++ UI

This folder contains the first original UI foundation for Bedrock++.

## Current files

- `_ui_defs.json`: registers Bedrock++ JSON UI definitions.
- `_global_variables.json`: shared colors and spacing tokens.
- `bedrockpp_theme.json`: reusable visual primitives for future screens.
- `bedrockpp/main_menu.json`: first main-menu layout module.

## Main-menu module status

The main-menu module defines a dark backplate, Bedrock++ title, subtitle, and three visual action tiles (Play, Worlds, Settings). It is currently a layout prototype: the tiles are not wired to Minecraft navigation actions, and the module is not yet attached to the vanilla start-screen composition.

## Visual direction

- Dark, neutral surfaces that still feel at home in Minecraft.
- A restrained green accent for focus and primary actions.
- Clear text hierarchy and generous spacing.
- Responsive layouts that can be adapted for touch, controller, and mouse.

## Important compatibility note

This is a JSON UI foundation, not an Ore UI implementation. JSON UI and Ore UI are different systems. A resource pack can register JSON UI files, but it cannot turn these files into React Facet components or directly replace hardcoded Ore UI screens.

No vanilla screen has been replaced yet. The next implementation step is to integrate and test one screen at a time against the target Bedrock version, starting with a supported JSON UI screen. These files have not yet been tested in Minecraft.
