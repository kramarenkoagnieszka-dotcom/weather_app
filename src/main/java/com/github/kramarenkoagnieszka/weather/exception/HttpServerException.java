package com.github.kramarenkoagnieszka.weather.exception;

public class HttpServerException extends HttpClientWrapperException {

  public HttpServerException(String message) {
    super(message);
  }
}
