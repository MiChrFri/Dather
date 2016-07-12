package com.example.michael.dather.API;

/**
 * Created by michael on 10/05/16.
 */

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class AsyncRequest {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final static String BASEURL = "http://frickm.de/";

    public Request apiRequest(String apiCall, String bodyString) throws Exception {
        RequestBody requestBody = RequestBody.create(JSON, bodyString);
        String urlString = BASEURL + apiCall;

        Request request = new Request.Builder()
                .url(urlString)
                .post(requestBody)
                .build();

        return request;
    }

    public Request urlGetRequest(String url) throws Exception {
        String urlString = url;

        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        return request;
    }
}
