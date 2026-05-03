package com.github.kramarenkoagnieszka.weather.app;

public final class AppConfig {

  private AppConfig() {
  }

  // API Request Keys
  public static final String QUERY_PARAMS_KEY = "queryStringParameters";
  public static final String QUERY_PARAM = "city";

  // HTTP Client Settings
  public static final long HTTP_CONNECT_TIMEOUT_SECONDS = 10L;
  public static final long HTTP_READ_TIMEOUT_SECONDS = 10L;

  // Retry Mechanism Settings
  public static final int DEFAULT_RETRIES = 3;
}
