package com.github.kramarenkoagnieszka.weather.service;

import com.github.kramarenkoagnieszka.weather.model.TemperatureCategory;

public class TemperatureClassifier {

  private static final double FREEZING_THRESHOLD = 0.0;
  private static final double COLD_THRESHOLD = 10.0;
  private static final double MILD_THRESHOLD = 20.0;
  private static final double WARM_THRESHOLD = 30.0;

  public TemperatureCategory classify(double temperature) {
    if (temperature < FREEZING_THRESHOLD) {
      return TemperatureCategory.FREEZING;
    }
    if (temperature < COLD_THRESHOLD) {
      return TemperatureCategory.COLD;
    }
    if (temperature < MILD_THRESHOLD) {
      return TemperatureCategory.MILD;
    }
    if (temperature < WARM_THRESHOLD) {
      return TemperatureCategory.WARM;
    }
    return TemperatureCategory.HOT;
  }
}
