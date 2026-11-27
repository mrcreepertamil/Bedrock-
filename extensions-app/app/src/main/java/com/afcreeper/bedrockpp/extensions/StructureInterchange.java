package com.afcreeper.bedrockpp.extensions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/** Writes Bedrock++ interchange JSON as uncompressed little-endian Bedrock NBT. */
final class StructureInterchange {
    private static final int MAX_BLOCKS = 32768;
    private static final int MAX_AXIS = 64;
    private static final int DEFAULT_BLOCK_VERSION = 18168865;
    private final int[] size = new int[3];
    private final int[] origin = new int[3];
    private final JSONArray palette;
    private final int[] primary;
    private final int[] secondary;
    private final String name;

    private StructureInterchange(JSONObject source) throws JSONException {
        if (!"bedrockpp-structure".equals(source.optString("format"))) {
            throw new JSONException("Not a Bedrock++ structure-data file.");
        }
        if (source.optInt("version", -1) != 1) {
            throw new JSONException("Unsupported structure-data version.");
        }
        name = source.optString("name", "structure");
        readTriple(source.optJSONArray("size"), size, "size");
        readTriple(source.optJSONArray("origin"), origin, "origin");

        long count = 1;
        for (int axis : size) {
            if (axis < 1 || axis > MAX_AXIS) {
                throw new JSONException("Each dimension must be between 1 and " + MAX_AXIS + ".");
            }
            count *= axis;
        }
        if (count > MAX_BLOCKS) throw new JSONException("Maximum supported size is " + MAX_BLOCKS + " blocks.");

        palette = source.optJSONArray("palette");
        JSONArray blocks = source.optJSONArray("blocks");
        if (palette == null || palette.length() == 0 || blocks == null) {
            throw new JSONException("A non-empty palette and blocks array are required.");
        }
        if (blocks.length() != (int) count) {
            throw new JSONException("Blocks array length does not match the dimensions.");
        }
        primary = readIndices(blocks, (int) count, palette.length(), "blocks");

        JSONArray second = source.optJSONArray("secondary");
        if (second == null) {
            secondary = new int[(int) count];
            java.util.Arrays.fill(secondary, -1);
        } else {
            if (second.length() != (int) count) {
                throw new JSONException("Secondary array length does not match the dimensions.");
            }
            secondary = readIndices(second, (int) count, palette.length(), "secondary");
        }

        for (int i = 0; i < palette.length(); i++) {
            JSONObject entry = palette.optJSONObject(i);
            if (entry == null || entry.optString("name").isEmpty()) {
                throw new JSONException("Palette entry " + i + " has no block name.");
            }
            if (entry.opt("states") != null && !(entry.opt("states") instanceof JSONObject)) {
                throw new JSONException("Palette states must be an object.");
            }
        }
    }

    static StructureInterchange parse(byte[] bytes) throws JSONException {
        return new StructureInterchange(new JSONObject(new String(bytes, StandardCharsets.UTF_8)));
    }

    String suggestedFileName() {
        String safe = name.replaceAll("[^A-Za-z0-9._-]+", "_").replaceAll("^[._-]+", "");
        return (safe.isEmpty() ? "structure" : safe) + ".mcstructure";
    }

    void writeTo(OutputStream destination) throws IOException, JSONException {
        DataOutputStream out = new DataOutputStream(destination);
        out.writeByte(10); // root compound
        writeString(out, "");
        writeIntTag(out, "format_version", 1);
        writeIntList(out, "size", size);

        writeTagHeader(out, 10, "structure");
        writeTagHeader(out, 9, "block_indices");
        out.writeByte(9); // list of lists
        writeLittleInt(out, 2);
        writeIntPayloadList(out, primary);
        writeIntPayloadList(out, secondary);

        writeEmptyList(out, "entities", 10);
        writeTagHeader(out, 10, "palette");
        writeTagHeader(out, 10, "default");
        writeTagHeader(out, 9, "block_palette");
        out.writeByte(10); // list of compounds
        writeLittleInt(out, palette.length());
        for (int i = 0; i < palette.length(); i++) writePaletteEntry(out, palette.getJSONObject(i));
        writeTagHeader(out, 10, "block_position_data");
        out.writeByte(0); // end block_position_data
        out.writeByte(0); // end default
        out.writeByte(0); // end palette
        out.writeByte(0); // end structure

        writeIntList(out, "structure_world_origin", origin);
        out.writeByte(0); // end root
        out.flush();
    }

    private static int[] readIndices(JSONArray values, int count, int paletteSize, String field)
            throws JSONException {
        int[] result = new int[count];
        for (int i = 0; i < count; i++) {
            int value = values.getInt(i);
            if (value < -1 || value >= paletteSize) {
                throw new JSONException(field + "[" + i + "] has an invalid palette index.");
            }
            result[i] = value;
        }
        return result;
    }

    private static void readTriple(JSONArray values, int[] target, String field) throws JSONException {
        if (values == null || values.length() != 3) {
            throw new JSONException(field + " must contain exactly three integers.");
        }
        for (int i = 0; i < 3; i++) target[i] = values.getInt(i);
    }

    private static void writePaletteEntry(DataOutputStream out, JSONObject entry)
            throws IOException, JSONException {
        writeStringTag(out, "name", entry.getString("name"));
        writeTagHeader(out, 10, "states");
        JSONObject states = entry.optJSONObject("states");
        if (states != null) {
            java.util.Iterator<String> keys = states.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = states.get(key);
                if (value instanceof Boolean) {
                    writeByteTag(out, key, (byte) ((Boolean) value ? 1 : 0));
                } else if (value instanceof Number) {
                    double number = ((Number) value).doubleValue();
                    if (number != Math.rint(number) || number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
                        throw new JSONException("Block state " + key + " must be an integer, boolean, or string.");
                    }
                    writeIntTag(out, key, (int) number);
                } else if (value instanceof String) {
                    writeStringTag(out, key, (String) value);
                } else {
                    throw new JSONException("Unsupported block state value: " + key);
                }
            }
        }
        out.writeByte(0);
        writeIntTag(out, "version", entry.optInt("version", DEFAULT_BLOCK_VERSION));
        out.writeByte(0);
    }

    private static void writeIntPayloadList(DataOutputStream out, int[] values) throws IOException {
        out.writeByte(3);
        writeLittleInt(out, values.length);
        for (int value : values) writeLittleInt(out, value);
    }

    private static void writeIntList(DataOutputStream out, String name, int[] values) throws IOException {
        writeTagHeader(out, 9, name);
        out.writeByte(3);
        writeLittleInt(out, values.length);
        for (int value : values) writeLittleInt(out, value);
    }

    private static void writeEmptyList(DataOutputStream out, String name, int elementType) throws IOException {
        writeTagHeader(out, 9, name);
        out.writeByte(elementType);
        writeLittleInt(out, 0);
    }

    private static void writeIntTag(DataOutputStream out, String name, int value) throws IOException {
        writeTagHeader(out, 3, name);
        writeLittleInt(out, value);
    }

    private static void writeByteTag(DataOutputStream out, String name, byte value) throws IOException {
        writeTagHeader(out, 1, name);
        out.writeByte(value);
    }

    private static void writeStringTag(DataOutputStream out, String name, String value) throws IOException {
        writeTagHeader(out, 8, name);
        writeString(out, value);
    }

    private static void writeTagHeader(DataOutputStream out, int type, String name) throws IOException {
        out.writeByte(type);
        writeString(out, name);
    }

    private static void writeString(DataOutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 65535) throw new IOException("NBT string is too long.");
        writeLittleShort(out, bytes.length);
        out.write(bytes);
    }

    private static void writeLittleShort(DataOutputStream out, int value) throws IOException {
        out.writeByte(value & 0xff);
        out.writeByte((value >>> 8) & 0xff);
    }

    private static void writeLittleInt(DataOutputStream out, int value) throws IOException {
        out.writeByte(value & 0xff);
        out.writeByte((value >>> 8) & 0xff);
        out.writeByte((value >>> 16) & 0xff);
        out.writeByte((value >>> 24) & 0xff);
    }
}
