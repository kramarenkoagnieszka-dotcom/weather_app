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

  static {
    TemperatureCategory[] values = values();
    for (int i = 1; i < values.length; i++) {
      if (values[i].upperThreshold <= values[i - 1].upperThreshold) {
        throw new IllegalStateException(String.format(
            "TemperatureCategory enum is not in ascending order: %s (%.1f) must be > %s (%.1f)",
            values[i].name(), values[i].upperThreshold,
            values[i - 1].name(), values[i - 1].upperThreshold
        ));
      }
    }
  }
}
