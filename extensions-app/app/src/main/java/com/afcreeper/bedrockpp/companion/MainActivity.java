package com.afcreeper.bedrockpp.companion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class MainActivity extends Activity {
    private static final int REQUEST_OPEN_INTERCHANGE = 1001;
    private static final int REQUEST_CREATE_STRUCTURE = 1002;
    private static final int MAX_INPUT_BYTES = 8 * 1024 * 1024;

    private TextView status;
    private Button exportButton;
    private StructureInterchange pendingStructure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(28, 36, 28, 28);

        TextView title = new TextView(this);
        title.setText("Bedrock++ Companion");
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 8, 0, 12);

        TextView intro = new TextView(this);
        intro.setText("Structure tools\n\nConvert a Bedrock++ structure-data JSON file into a portable Minecraft Bedrock .mcstructure file.");
        intro.setTextSize(16);
        intro.setGravity(Gravity.CENTER);
        intro.setPadding(0, 8, 0, 22);

        Button importButton = new Button(this);
        importButton.setText("Choose Bedrock++ structure data");
        importButton.setOnClickListener(view -> openInterchange());

        exportButton = new Button(this);
        exportButton.setText("Create .mcstructure file");
        exportButton.setEnabled(false);
        exportButton.setOnClickListener(view -> createStructureFile());

        status = new TextView(this);
        status.setText("No structure data selected.");
        status.setTextSize(14);
        status.setPadding(0, 22, 0, 0);
        status.setGravity(Gravity.CENTER);

        root.addView(title, matchWrap());
        root.addView(intro, matchWrap());
        root.addView(importButton, matchWrap());
        root.addView(exportButton, matchWrap());
        root.addView(status, matchWrap());
        setContentView(root);
    }

    private LinearLayout.LayoutParams matchWrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private void openInterchange() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, REQUEST_OPEN_INTERCHANGE);
    }

    private void createStructureFile() {
        if (pendingStructure == null) return;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/octet-stream");
        intent.putExtra(Intent.EXTRA_TITLE, pendingStructure.suggestedFileName());
        startActivityForResult(intent, REQUEST_CREATE_STRUCTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null || data.getData() == null) return;

        Uri uri = data.getData();
        if (requestCode == REQUEST_OPEN_INTERCHANGE) {
            readInterchange(uri);
        } else if (requestCode == REQUEST_CREATE_STRUCTURE) {
            writeStructure(uri);
        }
    }

    private void readInterchange(Uri uri) {
        try (InputStream input = getContentResolver().openInputStream(uri)) {
            if (input == null) throw new IOException("Could not open the selected file.");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int total = 0;
            int read;
            while ((read = input.read(buffer)) != -1) {
                total += read;
                if (total > MAX_INPUT_BYTES) throw new IOException("The selected file is too large.");
                bytes.write(buffer, 0, read);
            }
            pendingStructure = StructureInterchange.parse(bytes.toByteArray());
            exportButton.setEnabled(true);
            status.setText("Structure data validated. Choose where to save the .mcstructure file.");
        } catch (IOException | JSONException error) {
            pendingStructure = null;
            exportButton.setEnabled(false);
            status.setText("Could not read structure data: " + error.getMessage());
        }
    }

    private void writeStructure(Uri uri) {
        try (OutputStream output = getContentResolver().openOutputStream(uri, "w")) {
            if (output == null) throw new IOException("Could not create the destination file.");
            pendingStructure.writeTo(output);
            output.flush();
            status.setText("Created the .mcstructure file. Import it into a compatible Bedrock world or behavior pack.");
        } catch (IOException | JSONException error) {
            status.setText("Could not create the structure file: " + error.getMessage());
        }
    }
}
