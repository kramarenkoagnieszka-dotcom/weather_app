package com.github.kramarenkoagnieszka.weather.model;

import lombok.Getter;

@Getter
public class WeatherResponse {

  private final String city;
  private final double temperature;
  private final Unit unit;
  private final TemperatureCategory category;

  public WeatherResponse(String city, double temperature, TemperatureCategory category) {
    this.city = city;
    this.temperature = temperature;
    this.category = category;
    this.unit = Unit.CELSIUS;
  }
}
