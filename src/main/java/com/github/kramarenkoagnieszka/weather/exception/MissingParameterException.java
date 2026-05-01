package com.github.kramarenkoagnieszka.weather.exception;

public class MissingParameterException extends WeatherApplicationException {
  public MissingParameterException(String message) {
    super(message);
  }
}
