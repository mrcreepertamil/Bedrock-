/**
 * Bedrock++ Behavior Pack entry point.
 *
 * Feature modules should register their initializers before startup.
 * Import those modules here as they are implemented. The registry will
 * start only features enabled in this world's Behavior Pack settings.
 */
import { initializeEnabledFeatures } from "./feature_manager.js";

// Feature modules will be imported here when their implementations exist.
// Example:
// import "./features/cinematic_camera.js";

initializeEnabledFeatures().catch((error) => {
  console.error("[Bedrock++] Feature initialization failed:", error);
});
