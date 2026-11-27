/**
 * Bedrock++ feature settings
 *
 * Read the world's Behavior Pack settings before a feature starts.
 * Feature systems should call getFeatureSettings() and only initialize
 * functionality that the world owner has enabled.
 */
import { world } from "@minecraft/server";

export function getFeatureSettings() {
  const settings = world.getPackSettings();

  return Object.freeze({
    cinematicCamera: settings["bedrockpp:cinematic_camera"] === true,
    replayCapture: settings["bedrockpp:replay_capture"] === true,
    replayPlayback: settings["bedrockpp:replay_playback"] === true,
    actorTools: settings["bedrockpp:actor_tools"] === true,
    sceneTools: settings["bedrockpp:scene_tools"] === true
  });
}
