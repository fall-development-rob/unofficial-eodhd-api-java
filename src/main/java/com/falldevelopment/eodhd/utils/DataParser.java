package com.falldevelopment.eodhd.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;

import com.falldevelopment.eodhd.exceptions.ApiException;

public class DataParser {
    private final ObjectMapper objectMapper;
    private final Logger logger;

    public DataParser(ObjectMapper objectMapper, Logger logger) {
        this.objectMapper = objectMapper;
        this.logger = logger;
    }

    public <T> T parse(String responseBody, Class<T> responseType) throws ApiException {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            logger.warn("Empty response body");
            throw new ApiException("Empty response body");
        }

        try {
            return objectMapper.readValue(responseBody, responseType);
        } catch (IOException e) {
            logger.error("Failed to parse response: {}", e.getMessage());
            throw new ApiException("Failed to parse response", e);
        }
    }
}
