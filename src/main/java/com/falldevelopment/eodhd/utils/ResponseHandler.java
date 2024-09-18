package com.falldevelopment.eodhd.utils;

import com.falldevelopment.eodhd.exceptions.ApiException;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Map;

public class ResponseHandler {
    private final Logger logger;
    private final ObjectMapper objectMapper;

    public ResponseHandler(Logger logger, ObjectMapper objectMapper) {
        this.logger = logger;
        this.objectMapper = objectMapper;
    }

    public void handleErrorResponse(Response response) throws ApiException {
        int statusCode = response.code();
        String responseBody;

        try {
            responseBody = response.body() != null ? response.body().string() : "";
            Map<String, Object> json = objectMapper.readValue(responseBody, Map.class);

            if (json.containsKey("message")) {
                String message = (String) json.get("message");
                logger.error("({}) {}", statusCode, message);
                throw new ApiException("API error: " + message);
            } else if (json.containsKey("errors")) {
                logger.error("({}) Errors: {}", statusCode, json.get("errors"));
                throw new ApiException("API errors: " + json.get("errors"));
            } else {
                logger.error("({}) Unknown error: {}", statusCode, responseBody);
                throw new ApiException("Unknown API error with status code " + statusCode);
            }
        } catch (IOException e) {
            logger.error("Failed to parse error response: {}", e.getMessage());
            throw new ApiException("Invalid error response format", e);
        }
    }
}
