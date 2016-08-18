package com.example.michael.dather.UI;

import android.content.Context;
import android.content.Intent;
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
import com.example.michael.dather.MODEL.MySQLiteHelper;
import com.example.michael.dather.MODEL.User;
import com.example.michael.dather.R;

import org.json.JSONException;

public class AskActivity extends AppCompatActivity {
    CheckBox q[] = new CheckBox[9];

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

                try {
                    sendToServer(checkboxesToString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        q[0] = (CheckBox) findViewById(R.id.checkBox1);
        q[1] = (CheckBox) findViewById(R.id.checkBox2);
        q[2] = (CheckBox) findViewById(R.id.checkBox3);
        q[3] = (CheckBox) findViewById(R.id.checkBox4);
        q[4] = (CheckBox) findViewById(R.id.checkBox5);
        q[5] = (CheckBox) findViewById(R.id.checkBox6);
        q[6] = (CheckBox) findViewById(R.id.checkBox7);
        q[7] = (CheckBox) findViewById(R.id.checkBox8);
        q[8] = (CheckBox) findViewById(R.id.checkBox9);
    }

    private String checkboxesToString() {
        String resultString = "[{";

        resultString += "\"user_id\":\""+ getUserId() +"\",";
        resultString += "\"gmt_offset\":\""+ getGmtOffset() +"\",";

        int counter = 1;

        for(CheckBox question : q) {
            resultString += "\"q" + counter + "\":\"";
            resultString += question.isChecked() ? "true" : "false";
            resultString += "\",";

            counter+=1;
        }

        if (resultString != null && resultString.length() > 0 && resultString.charAt(resultString.length()-1)==',') {
            resultString = resultString.substring(0, resultString.length()-1);
        }

        resultString += "}]";

        return resultString;
    }

    private void sendToServer(String form) throws JSONException {
        if(isConnected() && form != "") {
            APIService apiService = new APIService(new ApiCallback() {
                @Override
                public void receivedResponse(Boolean success) {
                    if(success) {
                        getToMain();
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

            apiService.sendData(form, "user");
            showSnackbar("#31C154", "Sending . . .", Snackbar.LENGTH_SHORT);
        }
        else {
            showSnackbar("#F71114", "No internet connection", Snackbar.LENGTH_SHORT);
        }
    }

    private void getToMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
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
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("userID", null);
    }

    private String getGmtOffset(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("gmtOffset", null);
    }
}
