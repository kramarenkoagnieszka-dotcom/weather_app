package com.github.kramarenkoagnieszka.weather.exception;

public class WeatherUpstreamException extends WeatherApplicationException {

  public WeatherUpstreamException(String message, Throwable cause) {
    super(message, cause);
  }

  public WeatherUpstreamException(String message) {
    super(message);
  }
}
