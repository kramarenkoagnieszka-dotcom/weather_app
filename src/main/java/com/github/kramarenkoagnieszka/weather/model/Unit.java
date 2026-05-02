package com.github.kramarenkoagnieszka.weather.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Unit {
  CELSIUS("Celsius");

  private final String displayName;
}
