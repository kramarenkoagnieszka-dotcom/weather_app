package com.github.kramarenkoagnieszka.weather.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherResponse {

  public static final String DEFAULT_UNIT = "Celsius";

  private String city;
  private double temperature;

  @Builder.Default
  private String unit = DEFAULT_UNIT;

  private TemperatureCategory category;
}
