package com.github.kramarenkoagnieszka.weather.model;

import lombok.Value;

@Value
public class WeatherResponse {

  String city;
  double temperature;
  Unit unit;
  TemperatureCategory category;
}
