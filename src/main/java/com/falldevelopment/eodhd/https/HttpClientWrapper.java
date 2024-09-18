package com.falldevelopment.eodhd.https;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import com.falldevelopment.eodhd.interfaces.HttpClientInterface;

public class HttpClientWrapper implements HttpClientInterface {
    private final OkHttpClient httpClient;

    public HttpClientWrapper(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Response execute(Request request) throws IOException {
        return httpClient.newCall(request).execute();
    }
}
