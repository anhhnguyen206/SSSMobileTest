package com.example.anhhnguyen.myapplication;

import android.app.Activity;

import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by anhhnguyen on 7/13/2014.
 */
public class SSSActivity extends Activity {
    protected SpiceManager spiceManager = new SpiceManager(JacksonSpringAndroidSpiceService.class);

    protected void onStart(){
        super.onStart();
        spiceManager.start(this);
    }

    protected void onStop(){
        spiceManager.shouldStop();
        super.onStop();
    }
}
