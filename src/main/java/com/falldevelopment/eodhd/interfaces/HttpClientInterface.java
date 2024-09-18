package com.falldevelopment.eodhd.interfaces;

import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public interface HttpClientInterface {
    Response execute(Request request) throws IOException;
}
