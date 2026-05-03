package com.github.kramarenkoagnieszka.weather.exception;

public class WeatherApplicationException extends RuntimeException {

  public WeatherApplicationException(String message) {
    super(message);
  }

  public WeatherApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
