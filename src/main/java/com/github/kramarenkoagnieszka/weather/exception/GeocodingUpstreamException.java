package com.github.kramarenkoagnieszka.weather.exception;

public class GeocodingUpstreamException extends RuntimeException {

  public GeocodingUpstreamException(String message) {
    super(message);
  }

  public GeocodingUpstreamException(String message, Throwable cause) {
    super(message, cause);
  }
}
