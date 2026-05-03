package com.github.kramarenkoagnieszka.weather.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.kramarenkoagnieszka.weather.exception.MissingParameterException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherApplicationException;
import com.github.kramarenkoagnieszka.weather.model.WeatherRequest;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;

import java.util.Collections;
import java.util.Map;

public class WeatherQueryHandler implements RequestHandler<Map<String, Object>, WeatherResponse> {

  private static final ApplicationContext APP_CONTEXT = new ApplicationContext();

  @Override
  public WeatherResponse handleRequest(Map<String, Object> input, Context awsContext) {
    return executeSafely(awsContext, () -> {
      validateInput(input);

      Map<String, String> queryParams = extractQueryParams(input);
      String queryValue = queryParams.get(AppConfig.QUERY_PARAM);

      return APP_CONTEXT.getWeatherService().getWeather(new WeatherRequest(queryValue));
    });
  }

  private void validateInput(Map<String, Object> input) {
    if (input == null) {
      throw new MissingParameterException("Input cannot be null");
    }
    Map<String, String> queryParams = extractQueryParams(input);
    if (queryParams.isEmpty() || !queryParams.containsKey(AppConfig.QUERY_PARAM)
        || queryParams.get(AppConfig.QUERY_PARAM) == null || queryParams.get(AppConfig.QUERY_PARAM)
        .isBlank()) {
      throw new MissingParameterException(
          String.format("Missing required parameter: '%s'", AppConfig.QUERY_PARAM));
    }
  }

  private Map<String, String> extractQueryParams(Map<String, Object> input) {
    Object raw = input.get(AppConfig.QUERY_PARAMS_KEY);
    return (raw instanceof Map) ? (Map<String, String>) raw : Collections.emptyMap();
  }

  private WeatherResponse executeSafely(Context context, WeatherAction action) {
    try {
      if (context != null) {
        context.getLogger().log("Processing weather request...");
      }
      return action.execute();
    } catch (MissingParameterException e) {
      logError(context, "Validation error: ", e);
      throw new WeatherApplicationException("400 Bad Request: " + e.getMessage(), e);
    } catch (WeatherApplicationException e) {
      logError(context, "Service error: ", e);
      throw new WeatherApplicationException("500 Service Error: " + e.getMessage(), e);
    } catch (Exception e) {
      logError(context, "Fatal error: ", e);
      throw new WeatherApplicationException("500 Internal Server Error", e);
    }
  }

  private void logError(Context context, String message, Exception e) {
    if (context != null) {
      context.getLogger().log(message + e.getMessage());
    }
  }

  @FunctionalInterface
  private interface WeatherAction {

    WeatherResponse execute() throws Exception;
  }
}
