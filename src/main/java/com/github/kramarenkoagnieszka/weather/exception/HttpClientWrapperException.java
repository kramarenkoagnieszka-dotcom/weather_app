package com.github.kramarenkoagnieszka.weather.exception;

public class HttpClientWrapperException extends WeatherApplicationException {

  public HttpClientWrapperException(String message, Throwable cause) {
    super(message, cause);
  }

  public HttpClientWrapperException(String message) {
    super(message);
  }
}
