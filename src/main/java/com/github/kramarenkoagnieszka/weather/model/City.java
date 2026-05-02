package com.github.kramarenkoagnieszka.weather.model;

import lombok.Value;

@Value
public class City {

  String displayName;
  double latitude;
  double longitude;
}
