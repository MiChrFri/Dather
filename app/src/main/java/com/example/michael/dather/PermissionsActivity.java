package com.example.michael.dather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PermissionsActivity extends AppCompatActivity {


    CheckBox recordingPermission;
    final int PERMISSION_RECORD_AUDIO = 0;
    CheckBox locationPermission;
    final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    CheckBox internetPermission;
    final int PERMISSION_INTERNET = 2;


    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {


            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private void permissionRecordAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recordingPermission.setChecked(true);
        } else {
            recordingPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recordingPermission.isChecked()) {
                        ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
                    }
                }
            });
        }
    }

    private void permissionLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermission.setChecked(true);
        } else {
            locationPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (locationPermission.isChecked()) {
                        ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
                    }
                }
            });
        }
    }

    private void permissionInternet() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            internetPermission.setChecked(true);
        } else {
            internetPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (internetPermission.isChecked()) {
                        ActivityCompat.requestPermissions(PermissionsActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_INTERNET);
                    }
                }
            });
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permissions);
        mContentView = findViewById(R.id.fullscreen_content);

        TextView participantInfo = (TextView)findViewById(R.id.fullscreen_content);



        recordingPermission = (CheckBox) findViewById(R.id.checkBox1);
        permissionRecordAudio();

        locationPermission = (CheckBox) findViewById(R.id.checkBox2);
        permissionLocation();

        internetPermission = (CheckBox) findViewById(R.id.checkBox3);
        permissionInternet();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if(recordingPermission.isChecked() && locationPermission.isChecked() && internetPermission.isChecked()) {
            Button btn = (Button) findViewById(R.id.button);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View btn) {
                    SharedPreferences mPrefs = getSharedPreferences("prefs", 0);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    mEditor.putBoolean("permitted", true).commit();

                    getToMain();
                }
            });
        }

        switch (requestCode) {
            case PERMISSION_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recordingPermission.setChecked(true);
                }
                else {
                    recordingPermission.setChecked(false);
                }
                return;
            }
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission.setChecked(true);
                }
                else {
                    locationPermission.setChecked(false);
                }
                return;
            }
            case PERMISSION_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    internetPermission.setChecked(true);
                }
                else {
                    internetPermission.setChecked(false);
                }
                return;
            }
        }
    }


    private void getToMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

    private void confirmed() {
        SharedPreferences mPrefs = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putBoolean("acceptedTerms", true).commit();

        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }

}
