package com.example.michael.dather.API;

/**
 * Created by michael on 09/05/16.
 */

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIService extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();
    private ApiCallback callback;

    public APIService(ApiCallback callback){
        this.callback = callback;
    }

    public void ApiRequest(Request request) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.receivedResponse(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d("RESPONSE HEADER:", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String responseBody = response.body().string();

                Log.i("RESPONSE BODY:", responseBody);

                Matcher m = Pattern.compile("\"([^)]+)\"").matcher(responseBody);
                String responseTxt = "";
                while(m.find()) {
                    responseTxt = m.group(1);
                }

                Boolean returnValue = false;
                if (responseTxt.equals("SUCCESS")) {
                    returnValue = true;
                }

                callback.receivedResponse(returnValue);
            }
        });
    }

    public void sendData(String parameters, String url) {
        AsyncRequest asynchronousGet = new AsyncRequest();
        try {
            Request request =  asynchronousGet.apiRequest(url, parameters);
            ApiRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GETRequest(Request request) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.receivedResponse(false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        callback.receivedResponse(false);
                    }
                    else {
                        throw new IOException("Unexpected code " + response);
                    }
                }
                else {
                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        Log.d("RESPONSE HEADER:", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    String responseBody = response.body().string();

                    Boolean returnValue = false;

                    try {
                        JSONObject json = new JSONObject(responseBody);

                        if (json.getString("login").toString() != "") {
                            returnValue = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callback.receivedResponse(returnValue);
                }
            }
        });
    }

    public void urlRequest(String url) {
        AsyncRequest asynchronousGet = new AsyncRequest();

        try {
            Request request =  asynchronousGet.urlGetRequest(url);
            GETRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

