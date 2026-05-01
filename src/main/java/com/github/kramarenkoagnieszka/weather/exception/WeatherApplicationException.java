package com.github.kramarenkoagnieszka.weather.exception;

public class WeatherApplicationException extends Exception {

  public WeatherApplicationException(String message) {
    super(message);
  }

  public WeatherApplicationException(String message, Throwable cause) {
    super(message, cause);
  }
}
