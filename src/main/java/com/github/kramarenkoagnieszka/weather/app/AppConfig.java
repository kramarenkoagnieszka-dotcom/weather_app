package com.github.kramarenkoagnieszka.weather.app;


public final class AppConfig {

  private AppConfig() {
  }
  // HTTP Client Settings
  public static final long HTTP_CONNECT_TIMEOUT_SECONDS = 10L;

  // Retry Mechanism Settings
  public static final int DEFAULT_RETRIES = 3;
}
