package com.github.kramarenkoagnieszka.weather.exception;

public class HttpServerException extends RuntimeException {
  public HttpServerException(String message) {
    super(message);
  }
}
