package com.example.anhhnguyen.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.anhhnguyen.myapplication.util.ExceptionUtils;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import models.AuthenticatedUser;
import spicerequests.LoginRequest;

public class LoginActivity extends SSSActivity {
    private static final String KEY_LAST_REQUEST_CACHE_KEY = "lastRequestCacheKey";

    private String lastRequestCacheKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        // render activity_login.xml
        setContentView(R.layout.activity_login);

        final EditText username = (EditText)findViewById(R.id.username);
        final EditText password = (EditText)findViewById(R.id.password);

        final Button btnLogin = (Button)findViewById(R.id.sign_in_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                performLoginRequest(usernameValue, passwordValue);
            }
        });
    }

    private void performLoginRequest(String username, String password){
        LoginActivity.this.setProgressBarIndeterminateVisibility(true);

        LoginRequest requestLogin = new LoginRequest(username, password);
        lastRequestCacheKey = requestLogin.createCacheKey();

        super.spiceManager.execute(requestLogin, lastRequestCacheKey, DurationInMillis.ONE_MINUTE, new LoginRequestListener());
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
            spiceManager.addListenerIfPending(AuthenticatedUser.class,
                    lastRequestCacheKey, new LoginRequestListener());
            spiceManager.getFromCache(AuthenticatedUser.class,
                    lastRequestCacheKey, DurationInMillis.ONE_MINUTE,
                    new LoginRequestListener());
        }
    }

    private class LoginRequestListener implements RequestListener<AuthenticatedUser>{

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            String error = ExceptionUtils.getErrorMessage(spiceException);

            new AlertDialog.Builder(LoginActivity.this)
                    .setInverseBackgroundForced(true)
                    .setIcon(android.R.drawable.stat_notify_error)
                    .setTitle("Error")
                    .setMessage(error)
                    .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            LoginActivity.this.setProgressBarIndeterminateVisibility(false);
        }

        @Override
        public void onRequestSuccess(AuthenticatedUser authenticatedUser) {

            AuthenticatedUser.setCurrentUser(LoginActivity.this, authenticatedUser);

            LoginActivity.this.setProgressBarIndeterminateVisibility(false);

            // Create Intent to invoke ScannerActivity
            Intent intent = new Intent(LoginActivity.this, ScannerActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }
}
