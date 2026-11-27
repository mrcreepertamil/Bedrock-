# Bedrock++ UI Design Plan

## Goal

Create an original, vanilla-respecting UI enhancement layer for Minecraft Bedrock Edition. The reference pack informs the breadth and modular organization of the project, but Bedrock++ owns its layouts, textures, icons, names, and implementation.

## Visual direction

- Preserve Minecraft's recognizable visual language.
- Improve spacing, hierarchy, readability, and navigation rather than making every screen visually unrelated.
- Use a restrained dark-neutral foundation with Minecraft-inspired accent colors.
- Use clear focus, hover, pressed, disabled, and selected states.
- Prefer reusable components over one-off button and panel designs.
- Keep motion subtle and avoid animation that slows navigation.

## Planned screen groups

### 1. Start screen
- Clear Play, Settings, and other supported entry points.
- Strong visual hierarchy and uncluttered spacing.
- Responsive layout for touch, controller, and keyboard/mouse where supported.

### 2. Play and world selection
- Easier-to-scan world list.
- Clear world metadata and action placement.
- Search/filter only where the underlying screen and bindings support it.

### 3. Settings
- Consistent section navigation.
- More readable option rows and descriptions.
- Search and grouping only where supported by the target UI screen.

### 4. In-game interface
- Improve supported HUD and pause-screen presentation.
- Preserve important vanilla information and familiar interaction patterns.

## Component library

The first original components should be:
- Primary, secondary, and icon buttons
- Toggle and checkbox rows
- Section headers and navigation tabs
- Dialog and confirmation panel
- Search field
- Scrollable list row
- Tooltip and status indicator

Each component should define consistent dimensions, text treatment, focus behavior, and all interaction states.

## Implementation rules

1. Confirm that a screen/control is exposed to resource-pack UI customization before designing around it.
2. Use original identifiers and a Bedrock++ namespace.
3. Keep shared controls in reusable definition files.
4. Keep textures in a project-owned directory with documented source files.
5. Test on the declared minimum Minecraft version and record any newer-version differences.
6. Never copy reference-pack code or assets without explicit permission.
7. Keep native Ore UI / React Facet research distinct from the JSON UI work that can be shipped in a resource pack.

## First milestone

Build and test one small original UI component set before attempting a complete start-screen redesign. The first implementation should establish:
- A Bedrock++ namespace and definition registration
- A small shared button/panel style
- One supported screen integration
- A compatibility test checklist
