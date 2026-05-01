package com.github.kramarenkoagnieszka.weather.exception;

public class InvalidCityException extends WeatherApplicationException {
  public InvalidCityException(String message) {
    super(message);
  }
}
