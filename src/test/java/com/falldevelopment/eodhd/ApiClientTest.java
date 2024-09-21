package com.falldevelopment.eodhd;

import com.falldevelopment.eodhd.exceptions.ApiException;
import com.falldevelopment.eodhd.models.FundamentalsData;
import com.falldevelopment.eodhd.interfaces.*;

import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApiClientTest {

    @Test
    public void testGetFundamentals_Success() throws IOException, ApiException {
        // Mock the HttpClientInterface
        HttpClientInterface httpClientMock = mock(HttpClientInterface.class);

        // Prepare a mock response
        String jsonResponse = "{\"General\":{\"Code\":\"AAPL.US\",\"Type\":\"Common Stock\"},\"Highlights\":{\"MarketCapitalization\":2500000000000}}";
        ResponseBody responseBody = ResponseBody.create(jsonResponse, MediaType.get("application/json"));
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://eodhd.com/api/fundamentals/AAPL.US").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody)
                .build();

        // Configure the mock to return the mock response
        when(httpClientMock.execute(any(Request.class))).thenReturn(mockResponse);

        // Create the ApiClient with the mocked HttpClient
        ApiClient apiClient = new ApiClientBuilder()
                .apiKey("demo")
                .httpClient(httpClientMock)
                .build();

        // Call the get method
        FundamentalsData data = apiClient.getRest("fundamentals", "AAPL.US", null, FundamentalsData.class);

        // Verify the results
        assertNotNull(data);
        assertEquals("AAPL.US", data.getGeneral().getCode());
        assertEquals("Common Stock", data.getGeneral().getType());
        assertEquals(2500000000000L, data.getHighlights().getMarketCapitalization());
    }

    @Test
    public void testGetFundamentals_ApiException() throws IOException {
        // Mock the HttpClientInterface
        HttpClientInterface httpClientMock = mock(HttpClientInterface.class);

        // Prepare a mock error response
        String errorResponse = "{\"message\":\"Invalid API key\"}";
        ResponseBody responseBody = ResponseBody.create(errorResponse, MediaType.get("application/json"));
        Response mockResponse = new Response.Builder()
                .request(new Request.Builder().url("https://eodhd.com/api/fundamentals/AAPL.US").build())
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .body(responseBody)
                .build();

        // Configure the mock to return the mock error response
        when(httpClientMock.execute(any(Request.class))).thenReturn(mockResponse);

        // Create the ApiClient with the mocked HttpClient
        ApiClient apiClient = new ApiClientBuilder()
                .apiKey("invalid_api_key")
                .httpClient(httpClientMock)
                .build();

        // Call the get method and expect an ApiException
        ApiException exception = assertThrows(ApiException.class, () -> {
            apiClient.getRest("fundamentals", "AAPL.US", null, FundamentalsData.class);
        });

        // Verify the exception message
        assertTrue(exception.getMessage().contains("Invalid API key"));
    }
}
