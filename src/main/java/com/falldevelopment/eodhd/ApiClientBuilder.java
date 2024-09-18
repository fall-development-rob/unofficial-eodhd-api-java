package com.falldevelopment.eodhd;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import java.util.regex.Pattern;

import com.falldevelopment.eodhd.https.HttpClientWrapper;
import com.falldevelopment.eodhd.interfaces.HttpClientInterface;

public class ApiClientBuilder {
    private String apiKey;
    private String apiUrl = "https://eodhd.com/api";
    private HttpClientInterface httpClient = new HttpClientWrapper(new OkHttpClient());
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern API_KEY_PATTERN = Pattern.compile("^[A-Za-z0-9.]{16,32}$");

    public ApiClientBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ApiClientBuilder apiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }

    public ApiClientBuilder httpClient(HttpClientInterface httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public ApiClientBuilder objectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public ApiClient build() {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API key must be set");
        }
        validateApiKey(apiKey);
        return new ApiClient(apiKey, apiUrl, httpClient, objectMapper);
    }

    private void validateApiKey(String apiKey) {
        if (!"demo".equals(apiKey) && !API_KEY_PATTERN.matcher(apiKey).matches()) {
            throw new IllegalArgumentException("API key is invalid");
        }
    }
}
