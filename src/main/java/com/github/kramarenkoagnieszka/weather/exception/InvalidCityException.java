package com.github.kramarenkoagnieszka.weather.exception;

public class InvalidCityException extends RuntimeException {
  public InvalidCityException(String message) {
    super(message);
  }
}
