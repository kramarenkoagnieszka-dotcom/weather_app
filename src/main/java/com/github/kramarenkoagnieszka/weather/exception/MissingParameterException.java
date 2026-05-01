package com.github.kramarenkoagnieszka.weather.exception;

public class MissingParameterException extends RuntimeException {
  public MissingParameterException(String message) {
    super(message);
  }
}
