package com.github.kramarenkoagnieszka.weather.service;

import com.github.kramarenkoagnieszka.weather.client.GeocodingClient;
import com.github.kramarenkoagnieszka.weather.client.TemperatureClient;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherRequest;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherService {

  private final GeocodingClient geocodingClient;
  private final TemperatureClient temperatureClient;
  private final TemperatureClassifier classifier;

  public WeatherResponse getWeather(WeatherRequest weatherRequest) {
    City city = geocodingClient.getCity(weatherRequest);

    double temp = temperatureClient.getTemperature(city);

    return new WeatherResponse(city.getDisplayName(), temp, classifier.classify(temp));
  }
}
