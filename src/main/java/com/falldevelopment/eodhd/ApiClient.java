package com.falldevelopment.eodhd;

import com.falldevelopment.eodhd.interfaces.ApiClientInterface;
import com.falldevelopment.eodhd.interfaces.HttpClientInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.falldevelopment.eodhd.utils.*;
import com.falldevelopment.eodhd.https.*;
import com.falldevelopment.eodhd.exceptions.*;

public final class ApiClient implements ApiClientInterface {
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    private final String apiKey;
    private final String apiUrl;
    private final HttpClientInterface httpClient;
    private final DataParser dataParser;
    private final UrlBuilder urlBuilder;
    private final ResponseHandler responseHandler;

    // Private constructor accessible by the builder
    public ApiClient(String apiKey, String apiUrl, HttpClientInterface httpClient, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
        this.httpClient = httpClient;
        this.dataParser = new DataParser(objectMapper, logger);
        this.urlBuilder = new UrlBuilder(apiUrl, apiKey, logger);
        this.responseHandler = new ResponseHandler(logger, objectMapper);
    }

    @Override
    public <T> T getRest(String endpoint, String pathParameter, Map<String, String> queryParams, Class<T> responseType) throws ApiException {
        String url = urlBuilder.build(endpoint, pathParameter, queryParams);
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = httpClient.execute(request)) {
            if (!response.isSuccessful()) {
                responseHandler.handleErrorResponse(response);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            return dataParser.parse(responseBody, responseType);
        } catch (IOException e) {
            logger.error("Request failed: {}", e.getMessage());
            throw new ApiException("Request failed", e);
        }
    }

}
