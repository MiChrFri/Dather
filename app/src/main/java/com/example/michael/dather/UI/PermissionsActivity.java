package com.example.michael.dather.UI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.michael.dather.R;

public class PermissionsActivity extends AppCompatActivity {
    CheckBox recordingPermission;
    final int PERMISSION_RECORD_AUDIO = 0;
    CheckBox locationPermission;
    final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    CheckBox internetPermission;
    final int PERMISSION_INTERNET = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_permissions);

        recordingPermission = (CheckBox) findViewById(R.id.checkBox1);
        permissionRecordAudio();

        locationPermission = (CheckBox) findViewById(R.id.checkBox2);
        permissionLocation();

        internetPermission = (CheckBox) findViewById(R.id.checkBox3);
        permissionInternet();
    }

    private void permissionRecordAudio() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recordingPermission.setChecked(true);
            recordingPermission.setClickable(false);
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
            locationPermission.setClickable(false);
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
            internetPermission.setClickable(false);
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
                    recordingPermission.setClickable(false);
                }
                else {
                    recordingPermission.setChecked(false);
                }
                return;
            }
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermission.setChecked(true);
                    locationPermission.setClickable(false);
                }
                else {
                    locationPermission.setChecked(false);
                }
                return;
            }
            case PERMISSION_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    internetPermission.setChecked(true);
                    internetPermission.setClickable(false);
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
}
