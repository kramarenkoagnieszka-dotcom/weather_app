package com.github.kramarenkoagnieszka.weather.exception;

public class GeocodingClientException extends WeatherApplicationException {

  public GeocodingClientException(String message, Throwable cause) {
    super(message, cause);
  }
  public GeocodingClientException(String message) { super(message); }
}
