/**
 * Bedrock++ Structure Export (world capture prototype).
 *
 * Use a normal stick to select two opposite corners. The selected region is
 * saved into the current world's structure storage through the stable Script API.
 * This does not write a .mcstructure file to device storage.
 */
import { StructureSaveMode, world } from "@minecraft/server";
import { ModalFormData } from "@minecraft/server-ui";
import { registerFeature } from "../feature_manager.js";

const SELECTION_ITEM = "minecraft:stick";
const MAX_AXIS_SIZE = 64;
const MAX_BLOCK_COUNT = 32768;
const selections = new Map();

function getBounds(first, second) {
  const min = {
    x: Math.min(first.x, second.x),
    y: Math.min(first.y, second.y),
    z: Math.min(first.z, second.z)
  };
  const max = {
    x: Math.max(first.x, second.x),
    y: Math.max(first.y, second.y),
    z: Math.max(first.z, second.z)
  };
  const size = {
    x: max.x - min.x + 1,
    y: max.y - min.y + 1,
    z: max.z - min.z + 1
  };

  return { min, max, size };
}

function sanitizeName(value) {
  return String(value ?? "")
    .trim()
    .toLowerCase()
    .replace(/[^a-z0-9_.-]+/g, "_")
    .replace(/^[^a-z0-9]+/, "")
    .replace(/_+/g, "_")
    .slice(0, 48)
    .replace(/[_.-]+$/g, "");
}

function openExportForm(player, dimension, first, second) {
  const bounds = getBounds(first, second);
  const { x, y, z } = bounds.size;
  const blockCount = x * y * z;

  if (
    x > MAX_AXIS_SIZE ||
    y > MAX_AXIS_SIZE ||
    z > MAX_AXIS_SIZE ||
    blockCount > MAX_BLOCK_COUNT
  ) {
    player.sendMessage(
      `§c[Bedrock++] Selection is too large. Maximum: ${MAX_AXIS_SIZE} blocks per axis and ${MAX_BLOCK_COUNT} blocks total.`
    );
    return;
  }

  const form = new ModalFormData()
    .title("Bedrock++ Structure Export")
    .textField("Structure name", "my_build");

  form.show(player).then((response) => {
    if (response.canceled) {
      player.sendMessage("§7[Bedrock++] Structure export cancelled.");
      return;
    }

    const rawName = response.formValues?.[0];
    const name = sanitizeName(rawName);

    if (!name) {
      player.sendMessage("§c[Bedrock++] Enter a valid structure name.");
      return;
    }

    const identifier = `bedrockpp:${name}`;

    try {
      if (world.structureManager.get(identifier)) {
        player.sendMessage(
          `§c[Bedrock++] "${identifier}" already exists in this world. Choose another name.`
        );
        return;
      }

      world.structureManager.createFromWorld(
        identifier,
        dimension,
        bounds.min,
        bounds.max,
        {
          includeBlocks: true,
          includeEntities: false,
          saveMode: StructureSaveMode.World
        }
      );

      player.sendMessage(
        `§a[Bedrock++] Saved ${identifier} (${x}×${y}×${z}) to this world's structure storage.`
      );
      player.sendMessage(
        "§7This is a world save, not an external .mcstructure file yet."
      );
    } catch (error) {
      console.error("[Bedrock++] Structure export failed:", error);
      player.sendMessage(
        "§c[Bedrock++] Could not save this structure. Check the selected area and content log."
      );
    }
  }).catch((error) => {
    console.error("[Bedrock++] Could not open structure export form:", error);
    player.sendMessage("§c[Bedrock++] Could not open the structure export form.");
  });
}

registerFeature("bedrockpp:structure_export", async () => {
  world.afterEvents.playerInteractWithBlock.subscribe((event) => {
    if (!event.isFirstEvent) return;

    const heldItem = event.itemStack ?? event.beforeItemStack;
    if (heldItem?.typeId !== SELECTION_ITEM) return;

    const { player, block } = event;
    const point = {
      x: block.location.x,
      y: block.location.y,
      z: block.location.z
    };
    const previous = selections.get(player.id);

    if (!previous || previous.dimensionId !== player.dimension.id) {
      selections.set(player.id, {
        point,
        dimensionId: player.dimension.id
      });
      player.sendMessage(
        `§e[Bedrock++] First corner: ${point.x}, ${point.y}, ${point.z}. Use the stick on the opposite corner.`
      );
      return;
    }

    selections.delete(player.id);
    player.sendMessage("§e[Bedrock++] Second corner selected. Choose a name in the form.");
    openExportForm(player, player.dimension, previous.point, point);
  });

  world.afterEvents.playerLeave.subscribe((event) => {
    selections.delete(event.playerId);
  });

  console.log("[Bedrock++] Structure Export initialized.");
});
