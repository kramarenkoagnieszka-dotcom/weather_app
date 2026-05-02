package com.github.kramarenkoagnieszka.weather.exception;

public class WeatherClientException extends WeatherApplicationException {

  public WeatherClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public WeatherClientException(String message) {
    super(message);
  }
}
