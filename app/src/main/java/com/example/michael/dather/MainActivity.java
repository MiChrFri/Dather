package com.example.michael.dather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.example.michael.dather.API.APIService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton gatherBtn;
    FloatingActionButton stopBtn;
    FloatingActionButton sendBtn;

    boolean sensoring = false;
    Sensors sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences mPrefs = getSharedPreferences("prefs", 0);
        if(!mPrefs.getBoolean("acceptedTerms", false)) {
            Intent myIntent = new Intent(this, TermsOfUseActivity.class);
            startActivity(myIntent);
        }

        if(!mPrefs.getBoolean("permitted", false)) {
            Intent myIntent2 = new Intent(this, PermissionsActivity.class);
            startActivity(myIntent2);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupStartBtn();
        setupStopBtn();
        setupSendBtn();
    }


    private void setupStartBtn() {
        gatherBtn = (FloatingActionButton) findViewById(R.id.gather);
        gatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sensoring) {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(view, "Sensoring . . .", Snackbar.LENGTH_INDEFINITE);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#cc0000"));
                    snackbar.show();

                    sensoring = true;
                    gatherBtn.setVisibility(View.INVISIBLE);
                    stopBtn.setVisibility(View.VISIBLE);

                    Handler handler = new Handler();
                    Runnable r = new Runnable() {
                        public void run() {
                            try {
                                sensor = new Sensors(getApplicationContext(), 1000);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    handler.postDelayed(r, 500);
                }
            }
        });
    }


    private void setupStopBtn() {
        stopBtn = (FloatingActionButton) findViewById(R.id.stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sensoring) {
                    Snackbar snackbar;
                    snackbar = Snackbar.make(view, "Stopped sensoring", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#35AAF5"));
                    snackbar.show();

                    sensoring = false;
                    stopBtn.setVisibility(View.INVISIBLE);
                    sendBtn.setVisibility(View.VISIBLE);

                    sensor.running = false;
                }
            }
        });
    }

    private void setupSendBtn() {
        sendBtn = (FloatingActionButton) findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar;
                snackbar = Snackbar.make(view, "Sending . . .", Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#31C154"));
                snackbar.show();

                sendBtn.setVisibility(View.INVISIBLE);

                Handler handler=new Handler();
                Runnable r=new Runnable() {
                    public void run() {
                        try {
                            sendToServer(sensor.params);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(r, 500);
            }
        });
    }

    private void sendToServer(ArrayList<ArrayList<String>> dataSet) throws JSONException {
        JSONArray jsAr = new JSONArray();

        for(ArrayList<String> list : dataSet) {
            JSONObject obj = new JSONObject();
            obj.put("timestamp", list.get(0));
            obj.put("light", list.get(1));
            obj.put("steps", list.get(2));
            obj.put("volume", list.get(3));
            obj.put("accX", list.get(4));
            obj.put("accY", list.get(5));
            obj.put("accZ", list.get(6));
            obj.put("latitude", list.get(7));
            obj.put("longitude", list.get(8));

            jsAr.put(obj);
        }

        if(isConnected()) {
            APIService apiService = new APIService();
            apiService.sendData(jsAr.toString());
        }
        else {
            Snackbar snackbar;
            snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#F71114"));
            snackbar.show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected()? true : false;
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
