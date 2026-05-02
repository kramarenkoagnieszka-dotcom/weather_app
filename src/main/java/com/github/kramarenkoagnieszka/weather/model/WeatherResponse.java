package com.github.kramarenkoagnieszka.weather.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class WeatherResponse {

  private String city;
  private double temperature;
  private Unit unit;
  private TemperatureCategory category;

  public WeatherResponse (String city, double temperature, TemperatureCategory category){
    this.city = city;
    this.temperature = temperature;
    this.category = category;
    this.unit = Unit.CELSIUS;
  }
}
