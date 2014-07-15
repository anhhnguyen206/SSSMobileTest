package com.example.anhhnguyen.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.retry.DefaultRetryPolicy;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import models.AuthenticatedUser;
import spicelisteners.ScanRequestListener;
import spicerequests.ScanRequest;

import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ScannerActivity extends SSSActivity implements ZBarScannerView.ResultHandler {
    private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

    private String lastRequestCacheKey;

    private ZBarScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        // Check if it's already logged in.  If not, go back to LoginActivity
        boolean loggedIn = AuthenticatedUser.isLoggedIn(this);
        if (!loggedIn){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);

        final Button btnEnterCode = (Button)findViewById(R.id.btnEnterCode);
        btnEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannerActivity.this, EnterCodeActivity.class);
                startActivity(intent);
            }
        });

        // check if camera is available
        if (!isCameraAvailable()) {
            Toast.makeText(this, "Your device does not have a real-facing camera.  Closing.", Toast.LENGTH_SHORT);
            return;
        }

        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setupScanner();

        LinearLayout zBarLayout = (LinearLayout)findViewById(R.id.zBarLayout);
        zBarLayout.addView(mScannerView);
    }

    private boolean isCameraAvailable() {
        PackageManager packageManager = this.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scanner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, LogoutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        onPause();
        ScannerActivity.this.setProgressBarIndeterminateVisibility(true);

        String code = rawResult.getContents();

        ScanRequest scanRequest = new ScanRequest(code, AuthenticatedUser.getCurrentUser(ScannerActivity.this).getAccess_token());
        scanRequest.setRetryPolicy(new DefaultRetryPolicy(0,0,0));

        super.spiceManager.execute(scanRequest, new ScanRequestListener(this));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(lastRequestCacheKey)) {
            outState.putString(KEY_LAST_REQUEST_CACHE_KEY, lastRequestCacheKey);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(KEY_LAST_REQUEST_CACHE_KEY)) {
            lastRequestCacheKey = savedInstanceState
                    .getString(KEY_LAST_REQUEST_CACHE_KEY);
            spiceManager.addListenerIfPending(String.class,
                    lastRequestCacheKey, new ScanRequestListener(this));
            spiceManager.getFromCache(String.class,
                    lastRequestCacheKey, DurationInMillis.ONE_MINUTE,
                    new ScanRequestListener(this));
        }
    }

}
