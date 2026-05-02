package com.github.kramarenkoagnieszka.weather.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kramarenkoagnieszka.weather.client.GeocodingClient;
import com.github.kramarenkoagnieszka.weather.client.HttpClientWrapper;
import com.github.kramarenkoagnieszka.weather.client.OpenMeteoGeocodingClient;
import com.github.kramarenkoagnieszka.weather.client.OpenMeteoWeatherClient;
import com.github.kramarenkoagnieszka.weather.client.TemperatureClient;

import com.github.kramarenkoagnieszka.weather.service.TemperatureClassifier;
import com.github.kramarenkoagnieszka.weather.service.WeatherService;
import lombok.Getter;

import java.net.http.HttpClient;
import java.time.Duration;

@Getter
public class ApplicationContext {

  private final ObjectMapper objectMapper;
  private final HttpClient httpClient;
  private final HttpClientWrapper httpClientWrapper;
  private final TemperatureClient temperatureClient;
  private final GeocodingClient geocodingClient;
  private final TemperatureClassifier temperatureClassifier;
  private final WeatherService weatherService;

  public ApplicationContext() {
    this.objectMapper = new ObjectMapper();
    this.httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(AppConfig.HTTP_CONNECT_TIMEOUT_SECONDS))
        .build();
    this.httpClientWrapper = new HttpClientWrapper(httpClient);
    this.temperatureClient = new OpenMeteoWeatherClient(httpClientWrapper, objectMapper);
    this.geocodingClient = new OpenMeteoGeocodingClient(httpClientWrapper, objectMapper);
    this.temperatureClassifier = new TemperatureClassifier();
    this.weatherService = new WeatherService(geocodingClient, temperatureClient, temperatureClassifier);
  }
}
