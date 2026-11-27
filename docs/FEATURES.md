# Feature ideas and feasibility

Ideas are collected here before they become commitments. Each feature needs a prototype and a compatibility review.

## Interface and user experience
- Improve selected menus and UI flows.
- Review HUD and inventory presentation opportunities.
- Add consistent visual feedback and transitions where supported.

## Structure tools and schematics

### Structure Export
- Capture a selected cuboid region using the stable Bedrock Script API.
- Save the capture into the current world's structure storage.
- Use a simple in-game naming form.
- Keep portable `.mcstructure` file export as a separate companion-APK integration task until a supported Android transfer path is proven.

The first implementation is documented in [Structure Export](STRUCTURE_EXPORT.md). It is a prototype and still needs in-game testing; it does not yet create a standalone file in device storage.

### Bedrock++ Schematics
- Explore hologram previews and placement guidance.
- Show material lists, layers, and build progress.
- Support rotation and mirroring.
- Investigate importing and exporting portable structure files through the companion APK.

These are goals for investigation, not claims that Bedrock's add-on APIs currently expose every capability needed for a complete Litematica-style workflow.

## Cinematic and replay tools
Inspired by the creative workflows of Java Edition's Blockbuster and Replay Mod:
- Record or capture gameplay data, if technically feasible.
- Replay recorded sessions.
- Provide camera positioning and camera-path tools.
- Explore actor/scene workflows for machinima.
- Offer a timeline-style editing experience.

These are goals for investigation, not claims that Bedrock's add-on APIs currently expose the same capabilities as Java mods. Some ideas may require a companion tool or may prove impossible within a normal add-on.

## Future expansion
The project may add entirely new feature categories over time. New ideas should still follow the vanilla-first design principles in the main README.

## Feature proposal template
For each serious proposal, document:
- **Problem:** What does this improve?
- **Player benefit:** Why is it useful?
- **Vanilla fit:** Why does it belong in Bedrock?
- **Technical route:** Which supported APIs or tools could implement it?
- **Limitations:** What cannot be done?
- **Test plan:** How will it be verified in-game?
