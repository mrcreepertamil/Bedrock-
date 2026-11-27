/**
 * Bedrock++ feature registry and toggle gate.
 *
 * Feature modules register an initializer using their manifest setting key.
 * Only registered features whose world setting is exactly true are started.
 */
import { world } from "@minecraft/server";

const featureInitializers = new Map();

const KNOWN_FEATURE_SETTINGS = new Set([
  "bedrockpp:cinematic_camera",
  "bedrockpp:replay_capture",
  "bedrockpp:replay_playback",
  "bedrockpp:actor_tools",
  "bedrockpp:scene_tools",
  "bedrockpp:structure_export"
]);

export function getFeatureSettings() {
  const packSettings = world.getPackSettings();

  return Object.freeze({
    cinematicCamera: packSettings["bedrockpp:cinematic_camera"] === true,
    replayCapture: packSettings["bedrockpp:replay_capture"] === true,
    replayPlayback: packSettings["bedrockpp:replay_playback"] === true,
    actorTools: packSettings["bedrockpp:actor_tools"] === true,
    sceneTools: packSettings["bedrockpp:scene_tools"] === true,
    structureExport: packSettings["bedrockpp:structure_export"] === true
  });
}

/**
 * Register a feature initializer.
 * Call this from a feature module before initializeEnabledFeatures().
 */
export function registerFeature(settingKey, initializer) {
  if (!KNOWN_FEATURE_SETTINGS.has(settingKey)) {
    throw new Error(`Unknown Bedrock++ feature setting: ${settingKey}`);
  }

  if (typeof initializer !== "function") {
    throw new TypeError("A Bedrock++ feature initializer must be a function.");
  }

  if (featureInitializers.has(settingKey)) {
    throw new Error(`A feature is already registered for ${settingKey}`);
  }

  featureInitializers.set(settingKey, initializer);
}

/**
 * Start enabled, registered features only.
 * Disabled features are never passed to their initializer.
 */
export async function initializeEnabledFeatures() {
  const settings = getFeatureSettings();
  const startedFeatures = [];

  for (const [settingKey, initializer] of featureInitializers) {
    if (world.getPackSettings()[settingKey] !== true) {
      continue;
    }

    await initializer(Object.freeze({ world, settings }));
    startedFeatures.push(settingKey);
  }

  return Object.freeze(startedFeatures);
}
