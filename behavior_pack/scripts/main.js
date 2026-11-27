/**
 * Bedrock++ Behavior Pack entry point.
 *
 * Feature modules register their initializers before startup.
 * The registry starts only features enabled in this world's Behavior Pack settings.
 */
import { initializeEnabledFeatures } from "./feature_manager.js";
import "./features/structure_export.js";

// Import additional feature modules here as they are implemented.

initializeEnabledFeatures().catch((error) => {
  console.error("[Bedrock++] Feature initialization failed:", error);
});
