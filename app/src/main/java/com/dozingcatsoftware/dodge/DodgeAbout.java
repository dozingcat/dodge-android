package com.dozingcatsoftware.dodge;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.TextView;

public class DodgeAbout extends Activity  {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about);

        TextView tv = findViewById(R.id.aboutTextView);
        // Padding based on screen
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int padding = Math.min(metrics.widthPixels, metrics.heightPixels) / 25;
        tv.setPadding(padding, padding, padding, padding);

        // Use larger text on physically larger screens.
        float widthInches = metrics.widthPixels / metrics.xdpi;
        tv.setTextSize(widthInches > 4 ? 18f : 14f);
    }
}
