package com.github.kramarenkoagnieszka.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {
  private String displayName;
  private double latitude;
  private double longitude;
}
