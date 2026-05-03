package com.github.kramarenkoagnieszka.weather.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemperatureCategory {
  FREEZING(0.0),
  COLD(10.0),
  MILD(20.0),
  WARM(30.0),
  HOT(Double.MAX_VALUE);

  private final double upperThreshold;
}
