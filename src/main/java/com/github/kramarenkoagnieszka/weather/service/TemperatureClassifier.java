package com.github.kramarenkoagnieszka.weather.service;

import com.github.kramarenkoagnieszka.weather.model.TemperatureCategory;

public class TemperatureClassifier {

  public TemperatureCategory classify(double temperature) {
    for (TemperatureCategory category : TemperatureCategory.values()) {
      if (temperature < category.getUpperThreshold()) {
        return category;
      }
    }
    return TemperatureCategory.HOT;
  }
}
