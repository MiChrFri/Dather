package com.example.michael.dather.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.michael.dather.API.APIService;
import com.example.michael.dather.API.ApiCallback;
import com.example.michael.dather.R;

import org.json.JSONException;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getToMain();

//        if(getUserName() != null) {
//            getToMain();
//        }
//        else {
//            setContentView(R.layout.activity_login);
//
//            Button btn = (Button) findViewById(R.id.loginBtn);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View btn) {
//
//                    try {
//                        sendToServer();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
    }

    private void sendToServer() throws JSONException {

        EditText userNameTextField = (EditText) findViewById(R.id.username);
        String username = String.valueOf(userNameTextField.getText());

        if(isConnected() && username != "") {

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.commit();

            String url = "https://api.github.com/users/" + username;

            APIService apiService = new APIService(new ApiCallback() {
                @Override
                public void receivedResponse(Boolean success) {
                    if(success) {
                        getToMain();
                    }
                    else {
                        String message = "Your username seems to be incorrect";
                        String colorStr = "#ff0066";

                        Snackbar snackbar;
                        snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor(colorStr));
                        snackbar.show();
                    }
                }
            });

            apiService.urlRequest(url);
        }
        else {
            String message = "No connection or empty username field";
            String colorStr = "#ff0066";

            Snackbar snackbar;
            snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor(colorStr));
            snackbar.show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();

        return info != null && info.isConnected();
    }

    private String getUserName(){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    private void getToMain() {
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
}
