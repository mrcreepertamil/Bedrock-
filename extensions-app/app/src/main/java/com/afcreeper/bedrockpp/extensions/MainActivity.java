package com.afcreeper.bedrockpp.extensions;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(32, 32, 32, 32);

        TextView title = new TextView(this);
        title.setText("Bedrock++ Extensions");
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);

        TextView status = new TextView(this);
        status.setText("Extension manager foundation\n\nComing next: extension library, installs, updates and imports.");
        status.setTextSize(16);
        status.setGravity(Gravity.CENTER);
        status.setPadding(0, 24, 0, 0);

        root.addView(title);
        root.addView(status);
        setContentView(root);
    }
}
