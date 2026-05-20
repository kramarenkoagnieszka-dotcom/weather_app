package com.github.kramarenkoagnieszka.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kramarenkoagnieszka.weather.exception.GeocodingClientException;
import com.github.kramarenkoagnieszka.weather.exception.GeocodingUpstreamException;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OpenMeteoGeocodingClient implements GeocodingClient {

  private static final String GEO_URL =
      "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json";
  private static final String NODE_RESULTS = "results";
  private static final String NODE_LATITUDE = "latitude";
  private static final String NODE_LONGITUDE = "longitude";
  private static final String NODE_NAME = "name";
  private static final String NODE_REASON = "reason";

  private final HttpClientWrapper httpClientWrapper;
  private final ObjectMapper objectMapper;

  public OpenMeteoGeocodingClient(HttpClientWrapper httpClientWrapper, ObjectMapper objectMapper) {
    this.httpClientWrapper = httpClientWrapper;
    this.objectMapper = objectMapper.copy();
  }

  @Override
  public City getCity(WeatherRequest weatherRequest) {
    HttpResponse<String> response = fetchRawData(weatherRequest);

    if (response.statusCode() == 400) {
      String reason = extractReason(response.body());
      throw new GeocodingClientException("Geocoding API rejected request: " + reason);
    }

    if (response.statusCode() != 200) {
      throw new GeocodingUpstreamException(
          "Unexpected Geocoding API status: " + response.statusCode());
    }

    JsonNode root = parseJson(response.body());
    JsonNode results = root.path(NODE_RESULTS);

    if (results.isMissingNode() || !results.isArray() || results.isEmpty()) {
      throw new GeocodingClientException("City not found: " + weatherRequest.getCity());
    }

    JsonNode firstResult = results.get(0);
    double lat = firstResult.path(NODE_LATITUDE).asDouble(Double.NaN);
    double lon = firstResult.path(NODE_LONGITUDE).asDouble(Double.NaN);

    if (Double.isNaN(lat) || Double.isNaN(lon)) {
      throw new GeocodingUpstreamException(
          String.format("Geocoding result missing valid coordinates (%s/%s) for: %s",
              NODE_LATITUDE, NODE_LONGITUDE, weatherRequest.getCity()));
    }

    return new City(
        firstResult.path(NODE_NAME).asText(),
        lat,
        lon
    );
  }

  private HttpResponse<String> fetchRawData(WeatherRequest weatherRequest) {
    String encodedCity = URLEncoder.encode(weatherRequest.getCity(), StandardCharsets.UTF_8);
    String url = String.format(GEO_URL, encodedCity);

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

    return httpClientWrapper.sendWithRetry(request);
  }

  private String extractReason(String jsonBody) {
    try {
      return objectMapper.readTree(jsonBody).path(NODE_REASON).asText("Unknown reason");
    } catch (IOException e) {
      return "Could not parse error reason from API";
    }
  }

  private JsonNode parseJson(String jsonBody) {
    try {
      return objectMapper.readTree(jsonBody);
    } catch (IOException e) {
      throw new GeocodingUpstreamException("Failed to parse geocoding response body", e);
    }
  }
}
