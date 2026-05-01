package com.github.kramarenkoagnieszka.weather.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum City {
  WROCLAW("Wroclaw", 51.10, 17.03),
  WARSAW("Warsaw", 52.22, 21.01),
  KRAKOW("Krakow", 50.06, 19.94),
  LONDON("London", 51.50, -0.12),
  PARIS("Paris", 48.85, 2.35),
  BERLIN("Berlin", 52.52, 13.40),
  ROME("Rome", 41.90, 12.49),
  OSLO("Oslo", 59.91, 10.75),
  NEW_YORK("New York", 40.71, -74.00),
  DUBAI("Dubai", 25.20, 55.27),
  TOKYO("Tokyo", 35.67, 139.65);

  private final String displayName;
  private final double latitude;
  private final double longitude;

  public static Optional<City> fromString(String cityName) {
    if (cityName == null) {
      return Optional.empty();
    }
    String normalizedName = cityName.trim().replace(" ", "_");

    return Arrays.stream(City.values())
        .filter(city -> city.name().equalsIgnoreCase(normalizedName))
        .findFirst();
  }
}
