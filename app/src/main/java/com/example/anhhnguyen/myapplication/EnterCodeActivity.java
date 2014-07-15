package com.example.anhhnguyen.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.octo.android.robospice.retry.DefaultRetryPolicy;

import org.w3c.dom.Text;

import models.AuthenticatedUser;
import spicelisteners.ScanRequestListener;
import spicerequests.ScanRequest;

public class EnterCodeActivity extends SSSActivity {
    private EditText code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_enter_code);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        code = (EditText)findViewById(R.id.txtCodeNumber);

        code.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)){
                    String number = code.getText().toString();
                    performScanRequest(number);
                }

                return false;
            }
        });

        final Button send = (Button)findViewById(R.id.btnSend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = code.getText().toString();
                performScanRequest(number);
            }
        });
    }

    private void performScanRequest(String txtCode) {
        if (TextUtils.isEmpty(txtCode)){
            code.setError("Required");
            code.requestFocus();
            return;
        }

        EnterCodeActivity.this.setProgressBarIndeterminateVisibility(true);

        ScanRequest scanRequest = new ScanRequest(txtCode, AuthenticatedUser.getCurrentUser(EnterCodeActivity.this).getAccess_token());
        scanRequest.setRetryPolicy(new DefaultRetryPolicy(0,0,0));

        super.spiceManager.execute(scanRequest, new ScanRequestListener(this));
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
