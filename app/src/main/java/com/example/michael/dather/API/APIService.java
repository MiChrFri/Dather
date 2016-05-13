package com.example.michael.dather.API;

/**
 * Created by michael on 09/05/16.
 */

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.json.JSONObject;
import java.io.IOException;
import javax.security.auth.callback.Callback;
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

    public APIService() {}

    public void ApiRequest(Request request) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.receivedResponse("?");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.d("RESPONSE HEADER:", responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                String responseBody = response.body().string();
                if (responseBody == null) {
                    responseBody = "";
                }

                Log.i("D", responseBody);

                //callback.receivedResponse(responseBody);
            }
        });
    }

    public void sendData(String parameters) {
        AsyncRequest asynchronousGet = new AsyncRequest();
        try {
            Request request =  asynchronousGet.apiRequest("/dodo", parameters);
            ApiRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected JSONObject getParamsJSON(String[] keys, String[] values) {
        int numberOfParameters = keys.length;

        if(numberOfParameters > 0) {
            try{
                JSONObject paramsJson = new JSONObject();

                // add key value pairs to JSONObject
                for(int i = 0; i < numberOfParameters; i++) {
                    if(i <= values.length) {
                        paramsJson.put(keys[i], values[i]);
                    }
                    else {
                        paramsJson.put(keys[i], "");
                    }
                }

                return paramsJson;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

