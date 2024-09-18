package com.falldevelopment.eodhd.utils;

import okhttp3.HttpUrl;
import org.slf4j.Logger;

import java.util.Map;

public class UrlBuilder {
    private final String apiUrl;
    private final String apiKey;
    private final Logger logger;

    public UrlBuilder(String apiUrl, String apiKey, Logger logger) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.logger = logger;
    }

    public String build(String endpoint, String pathParameter, Map<String, String> queryParams) {
        HttpUrl baseUrl = HttpUrl.parse(apiUrl);
        if (baseUrl == null) {
            throw new IllegalStateException("Invalid API base URL");
        }

        HttpUrl.Builder urlBuilder = baseUrl.newBuilder()
                .addPathSegments(endpoint);

        if (pathParameter != null && !pathParameter.isEmpty()) {
            urlBuilder.addPathSegment(pathParameter);
        }

        urlBuilder.addQueryParameter("api_token", apiKey);

        if (queryParams != null) {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }

        HttpUrl url = urlBuilder.build();

        // Mask API key in logs
        String maskedUrl = url.toString().replaceAll("api_token=[^&]*", "api_token=****");
        logger.debug("Built URL: {}", maskedUrl);

        return url.toString();
    }
}
