package com.example.michael.dather.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.example.michael.dather.API.APIService;
import com.example.michael.dather.API.ApiCallback;
import com.example.michael.dather.R;

import org.json.JSONException;

public class AskActivity extends AppCompatActivity {

    CheckBox q[] = new CheckBox[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Log.i("CHECKBOXES", checkboxesToString());
            }
        });

        q[0] = (CheckBox) findViewById(R.id.checkBox1);
        q[1] = (CheckBox) findViewById(R.id.checkBox2);
        q[2] = (CheckBox) findViewById(R.id.checkBox3);
        q[3] = (CheckBox) findViewById(R.id.checkBox4);
        q[4] = (CheckBox) findViewById(R.id.checkBox5);
        q[5] = (CheckBox) findViewById(R.id.checkBox6);
    }

    private String checkboxesToString() {
        String resultString = "";

        for(CheckBox question : q) {

            resultString += question.isChecked() ? "YES | " : "NO | ";
        }

        return resultString;
    }

    private void sendToServer(String form) throws JSONException {
        if(isConnected() && form != "") {
            APIService apiService = new APIService(new ApiCallback() {
                @Override
                public void receivedResponse(Boolean success) {
                    if(success) {

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

            apiService.sendData(form);
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

    private String getUserId(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("userID", Context.MODE_PRIVATE);
        return preferences.getString("userID", null);
    }

}
