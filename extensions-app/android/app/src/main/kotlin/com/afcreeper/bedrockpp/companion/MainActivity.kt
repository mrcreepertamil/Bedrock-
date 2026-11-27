package com.afcreeper.bedrockpp.companion

import android.app.Activity
import android.content.Intent
import android.net.Uri
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : FlutterActivity() {
    private val channelName = "bedrockpp/structures"
    private val maxInputBytes = 8 * 1024 * 1024
    private val requestOpen = 7101
    private val requestSave = 7102

    private var pendingStructure: StructureInterchange? = null
    private var pendingPickResult: MethodChannel.Result? = null
    private var pendingSaveResult: MethodChannel.Result? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "pickStructureData" -> {
                        if (pendingPickResult != null) {
                            result.error("BUSY", "A file selection is already open.", null)
                        } else {
                            pendingPickResult = result
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "*/*"
                                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/json", "text/plain"))
                            }
                            startActivityForResult(intent, requestOpen)
                        }
                    }
                    "saveMcstructure" -> {
                        val structure = pendingStructure
                        if (structure == null) {
                            result.error("NO_STRUCTURE", "Import structure data first.", null)
                        } else if (pendingSaveResult != null) {
                            result.error("BUSY", "A save dialog is already open.", null)
                        } else {
                            pendingSaveResult = result
                            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "application/octet-stream"
                                putExtra(Intent.EXTRA_TITLE, structure.suggestedFileName())
                            }
                            startActivityForResult(intent, requestSave)
                        }
                    }
                    else -> result.notImplemented()
                }
            }
    }

    @Deprecated("Deprecated by Android, retained for broad SAF compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != requestOpen && requestCode != requestSave) return

        val uri = if (resultCode == Activity.RESULT_OK) data?.data else null
        if (requestCode == requestOpen) {
            val callback = pendingPickResult
            pendingPickResult = null
            if (uri == null) callback?.success(null) else readStructureData(uri, callback)
        } else {
            val callback = pendingSaveResult
            pendingSaveResult = null
            if (uri == null) callback?.success(null) else writeStructureFile(uri, callback)
        }
    }

    private fun readStructureData(uri: Uri, callback: MethodChannel.Result?) {
        try {
            val input = contentResolver.openInputStream(uri)
                ?: throw IOException("Could not open the selected file.")
            val bytes = input.use { stream ->
                val output = ByteArrayOutputStream()
                val buffer = ByteArray(8192)
                var total = 0
                while (true) {
                    val count = stream.read(buffer)
                    if (count < 0) break
                    total += count
                    if (total > maxInputBytes) throw IOException("The selected file is too large.")
                    output.write(buffer, 0, count)
                }
                output.toByteArray()
            }
            val parsed = StructureInterchange.parse(bytes)
            pendingStructure = parsed
            callback?.success(
                mapOf(
                    "name" to parsed.structureName(),
                    "size" to parsed.structureSize().toList(),
                    "blockCount" to parsed.blockCount(),
                    "paletteCount" to parsed.paletteCount()
                )
            )
        } catch (error: Exception) {
            pendingStructure = null
            callback?.error("IMPORT_FAILED", error.message ?: "Could not read structure data.", null)
        }
    }

    private fun writeStructureFile(uri: Uri, callback: MethodChannel.Result?) {
        val structure = pendingStructure
        if (structure == null) {
            callback?.error("NO_STRUCTURE", "Import structure data first.", null)
            return
        }
        try {
            val output = contentResolver.openOutputStream(uri, "w")
                ?: throw IOException("Could not create the destination file.")
            output.use { structure.writeTo(it) }
            callback?.success("Created " + structure.suggestedFileName())
        } catch (error: Exception) {
            callback?.error("EXPORT_FAILED", error.message ?: "Could not write the structure file.", null)
        }
    }
}
