package com.example.michael.dather.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.michael.dather.API.APIService;
import com.example.michael.dather.API.ApiCallback;
import com.example.michael.dather.MODEL.MySQLiteHelper;
import com.example.michael.dather.R;
import com.example.michael.dather.Sensors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton gatherBtn;
    FloatingActionButton stopBtn;
    FloatingActionButton sendBtn;
    FloatingActionButton restartBtn;


    TextView light = (TextView) findViewById(R.id.light);
    TextView volume = (TextView) findViewById(R.id.volume);
    TextView steps = (TextView) findViewById(R.id.steps);
    TextView acc = (TextView) findViewById(R.id.acc);
    TextView location = (TextView) findViewById(R.id.location);


    String dataString;
    boolean sensoring = false;
    public Sensors sensor;
    private TextView userName;
    MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mySQLiteHelper = new MySQLiteHelper(this);
        SharedPreferences mPrefs = getSharedPreferences("prefs", 0);

        if(!mPrefs.getBoolean("acceptedTerms", false)) {
            Intent myIntent = new Intent(getApplicationContext(), TermsOfUseActivity.class);
            startActivity(myIntent);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!mPrefs.getBoolean("permitted", false)) {
                Intent myIntent2 = new Intent(getApplicationContext(), PermissionsActivity.class);
                startActivity(myIntent2);
            }
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupLogoutBtn();
        setupStartBtn();
        setupStopBtn();
        setupSendBtn();
        setupRestartBtn();

        userName = (TextView) findViewById(R.id.username);

        if(getUserName() != null) {
            userName.setText("Hi " + getUserName() + "!");
        }

        if(mySQLiteHelper.hasEntries()) {
            readDataEntries();
            gatherBtn.setVisibility(View.INVISIBLE);
            stopBtn.setVisibility(View.INVISIBLE);
            sendBtn.setVisibility(View.VISIBLE);
            restartBtn.setVisibility(View.VISIBLE);
        }
        else {
            gatherBtn.setVisibility(View.VISIBLE);
            stopBtn.setVisibility(View.INVISIBLE);
            sendBtn.setVisibility(View.INVISIBLE);
            restartBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setupLogoutBtn() {
        Button logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void setupStartBtn() {
        gatherBtn = (FloatingActionButton) findViewById(R.id.gather);
        gatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGathering();
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

                    readDataEntries();

                    sensor.running = false;
                }
            }
        });
    }

    private void readDataEntries() {
        try {
            ArrayList<ArrayList<String>> entries = mySQLiteHelper.getAllEntries();

            dataString = datasetToJSONStr(entries);

            updateEntries(entries);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateEntries(ArrayList<ArrayList<String>> dataSet) {
        for(ArrayList<String> list : dataSet) {

            light.setText("light: " + list.get(2));
            volume.setText("volume: " + list.get(4));
            steps.setText("steps: " + list.get(3));
            acc.setText("acc: " + list.get(4) + "|" +  list.get(5) + "|" + list.get(6));
            location.setText("location: " + list.get(8) + "|" + list.get(9));
        }
    }


    private void setupSendBtn() {
        sendBtn = (FloatingActionButton) findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartBtn.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                Runnable r = new Runnable() {
                    public void run() {
                        try {
                            sendToServer();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(r, 0);
            }
        });
    }

    private void setupRestartBtn() {
        restartBtn = (FloatingActionButton) findViewById(R.id.gatherAgain);
        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGathering();
                stopBtn.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void startGathering() {
        //final String userId = genUserID();
        final String userId = "demo";
        saveStringId(genUserID(), "userID");
        saveStringId(getGMTOffset(), "gmtOffset");

        if(userId == null) {
            showSnackbar("#31C154", "Please, add your email address first", Snackbar.LENGTH_SHORT);
        }
        else if(!sensoring) {
            showSnackbar("#cc0000", "Sensoring . . .", Snackbar.LENGTH_INDEFINITE);

            sensoring = true;
            gatherBtn.setVisibility(View.INVISIBLE);
            stopBtn.setVisibility(View.VISIBLE);
            restartBtn.setVisibility(View.INVISIBLE);

            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        sensor = new Sensors(getApplicationContext(), 1000, userId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(r, 0);
        }
    }

    private void logout() {
        mySQLiteHelper.clearTable();
        removeUser();

        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }

    private void getToAsk() {
        Intent myIntent = new Intent(this, AskActivity.class);
        startActivity(myIntent);
    }

    private String datasetToJSONStr(ArrayList<ArrayList<String>> dataSet) throws JSONException {
        JSONArray jsAr = new JSONArray();

        for(ArrayList<String> list : dataSet) {
            JSONObject obj = new JSONObject();
            obj.put("user_id", getUserID());
            obj.put("timestamp", list.get(1));
            obj.put("light", list.get(2));
            obj.put("steps", list.get(3));
            obj.put("volume", list.get(4));
            obj.put("accX", list.get(5));
            obj.put("accY", list.get(6));
            obj.put("accZ", list.get(7));
            obj.put("latitude", list.get(8));
            obj.put("longitude", list.get(9));
            obj.put("secret", list.get(10));

            jsAr.put(obj);
        }
        return jsAr.toString();
    }

    private void sendToServer() throws JSONException {
        if(isConnected() && dataString != "") {
            APIService apiService = new APIService(new ApiCallback() {
                @Override
                public void receivedResponse(Boolean success) {
                    if(success) {
                        getToAsk();
                    }
                    else {
                        String message = "failed to send";
                        String colorStr = "#ff0066";
                        Snackbar snackbar;
                        snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor(colorStr));
                        snackbar.show();
                    }
                }
            });

            apiService.sendData(dataString, "dodo");
            showSnackbar("#31C154", "Sending . . .", Snackbar.LENGTH_SHORT);
        }
        else {
            showSnackbar("#F71114", "No internet connection", Snackbar.LENGTH_SHORT);
        }
    }

    private void showSnackbar(String colorCode, String message, int length) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, length);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor(colorCode));
        snackbar.show();
    }

    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    private String genUserID() {
        String email = String.valueOf(userName.getText());

        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(email.getBytes("UTF-8"));

            byte[] hash = md.digest();
            BigInteger bigInt = new BigInteger(1, hash);

            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getGMTOffset() {
        TimeZone tz = TimeZone.getDefault();
        Date now = new Date();
        int offsetFromUtc = tz.getOffset(now.getTime()) / 1000 / 60;

        return String.valueOf(offsetFromUtc);
    }

    private void saveStringId(String inptStr, String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, inptStr);
        editor.commit();
    }

    private String getUserID(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("userID", null);
    }

    private String getUserName(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    private void removeUser() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("userID");
        editor.remove("username");
        editor.apply();
    }
}
