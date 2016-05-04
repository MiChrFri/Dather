package com.example.michael.dather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    CheckBox recordingPermission;
    final int PERMISSION_RECORD_AUDIO = 0;
    CheckBox locationPermission;
    final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recordingPermission = (CheckBox) findViewById(R.id.checkBox1);
        permissionRecordAudio();

        locationPermission = (CheckBox) findViewById(R.id.checkBox2);
        permissionLocation();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Sensors sensors = new Sensors(getApplicationContext(), 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Snackbar.make(view, "START SENSORING", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
                    recordingPermission.setChecked(true);
                }
                else {
                    recordingPermission.setChecked(false);
                }
                return;
            }
        }
    }

    private void permissionRecordAudio() {
        Log.i("PERMISSION", "ASK");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            recordingPermission.setChecked(true);
            Log.i("PERMISSION", "HAVE IT");
        } else {
            Log.i("PERMISSION", "ELSE");
            recordingPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recordingPermission.isChecked()) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO);
                    }
                }
            });
        }
    }

    private void permissionLocation() {
        Log.i("PERMISSION", "ASK");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermission.setChecked(true);
            Log.i("PERMISSION", "HAVE IT");
        } else {
            Log.i("PERMISSION", "ELSE");
            locationPermission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (locationPermission.isChecked()) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
                    }
                }
            });
        }
    }


/* NOT USED AT THIS TIME
    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("myFile.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("LALA", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("ASDAS", "Can not read file: " + e.toString());
        }


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"frickm@tcd.ie"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , ret);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

        return ret;
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("myFile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data + "\r\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("FAILED", "File write failed: " + e.toString());
        }

    }
    */

}
