package com.falldevelopment.eodhd.exceptions;

public class ApiException extends Exception{

    private final int statusCode;
    private final String apiUrl;

    /**
     * Constructs an ApiException with detailed information.
     *
     * @param statusCode HTTP status code returned by the API.
     * @param apiUrl     The API URL that was called.
     * @param message    Detailed error message.
     */
    public ApiException(int statusCode, String apiUrl, String message) {
        super(message);
        this.statusCode = statusCode;
        this.apiUrl = apiUrl;
    }

    public int getStatusCode() {
        return this.statusCode;

    }

    public String getApiUrl() {
        return this.apiUrl;
    }
    
}
