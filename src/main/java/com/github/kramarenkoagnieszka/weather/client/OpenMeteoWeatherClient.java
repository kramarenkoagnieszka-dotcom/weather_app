package com.github.kramarenkoagnieszka.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherUpstreamException;
import com.github.kramarenkoagnieszka.weather.model.City;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class OpenMeteoWeatherClient implements TemperatureClient {

  private static final String OPEN_METEO_URL =
      "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current=temperature_2m";
  private static final String NODE_CURRENT = "current";
  private static final String NODE_TEMPERATURE = "temperature_2m";

  private final HttpClientWrapper httpClientWrapper;
  private final ObjectMapper objectMapper;

  public OpenMeteoWeatherClient(HttpClientWrapper httpClientWrapper, ObjectMapper objectMapper) {
    this.httpClientWrapper = httpClientWrapper;
    this.objectMapper = objectMapper.copy();
  }

  @Override
  public double getTemperature(City city) {
    HttpResponse<String> response = fetchRawData(city);

    if (response.statusCode() == 400) {
      throw new WeatherClientException(
          "Weather API rejected request for coordinates: "
              + city.getLatitude() + "," + city.getLongitude());
    }

    if (response.statusCode() != 200) {
      throw new WeatherUpstreamException(
          "Unexpected Weather API status: " + response.statusCode());
    }

    JsonNode root = parseJson(response.body());
    JsonNode temperatureNode = root.path(NODE_CURRENT).path(NODE_TEMPERATURE);

    if (temperatureNode.isMissingNode()) {
      throw new WeatherUpstreamException(
          String.format("Invalid response: '%s.%s' data is missing",
              NODE_CURRENT, NODE_TEMPERATURE));
    }

    return temperatureNode.asDouble();
  }

  private HttpResponse<String> fetchRawData(City city) {
    String url = String.format(Locale.US, OPEN_METEO_URL,
        city.getLatitude(), city.getLongitude());

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .build();

    return httpClientWrapper.sendWithRetry(request);
  }

  private JsonNode parseJson(String jsonBody) {
    try {
      return objectMapper.readTree(jsonBody);
    } catch (IOException e) {
      throw new WeatherUpstreamException("Failed to parse weather response body", e);
    }
  }
}
