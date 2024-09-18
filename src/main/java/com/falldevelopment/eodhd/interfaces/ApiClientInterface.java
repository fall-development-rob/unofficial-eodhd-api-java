package com.falldevelopment.eodhd.interfaces;

import java.util.Map;

import com.falldevelopment.eodhd.exceptions.ApiException;

public interface ApiClientInterface {

    <T> T getRest(String endpoint, String pathParameter, Map<String, String> queryParams, Class<T> responseType) throws ApiException;
    // Additional method signatures for POST, PUT, DELETE if needed

    
} 
