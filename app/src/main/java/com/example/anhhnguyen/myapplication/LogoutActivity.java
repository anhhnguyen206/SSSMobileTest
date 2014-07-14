package com.example.anhhnguyen.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import models.AuthenticatedUser;

public class LogoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        final TextView loggedMsg = (TextView)findViewById(R.id.loginMsg);

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.sss_mobile_test), 0);

        String username = preferences.getString(getResources().getString(R.string.username), "");

        loggedMsg.setText(Html.fromHtml("Logged in as: <b>"+ username +"</b>"));

        final Button logoutBtn = (Button)findViewById(R.id.btnLogOut);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LogoutActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AuthenticatedUser.setCurrentUser(LogoutActivity.this, null);

                                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                startActivity(intent);

                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
}
