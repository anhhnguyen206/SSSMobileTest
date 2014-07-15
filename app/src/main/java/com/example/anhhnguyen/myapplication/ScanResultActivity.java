package com.example.anhhnguyen.myapplication;

import com.example.anhhnguyen.myapplication.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ScanResultActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_result);

        String resultMessage = getIntent().getStringExtra("message");
        String resultType = getIntent().getStringExtra("type");

        final TextView resultText = (TextView)findViewById(R.id.fullscreen_content);
        resultText.setText(resultMessage);

        final LinearLayout wrapperLayout = (LinearLayout)findViewById(R.id.wrapper);
        wrapperLayout.setBackgroundColor(Color.parseColor("#CC0000"));

        final ImageView imageStatus = (ImageView)findViewById(R.id.imageStatus);
        imageStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_invalid));

        if (resultType.equals("valid")){
            wrapperLayout.setBackgroundColor(Color.parseColor("#669900"));
            imageStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_valid));
        }

        //resultText.setcom(drawable, null, null, null);

        wrapperLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scanNext();
                return false;
            }
        });

        final Button button = (Button)findViewById(R.id.scan_next_ticket);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNext();
            }
        });
    }

    private void scanNext() {
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivity(intent);
    }


 }
