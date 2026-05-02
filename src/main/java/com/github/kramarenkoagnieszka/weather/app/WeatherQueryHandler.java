package com.github.kramarenkoagnieszka.weather.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.kramarenkoagnieszka.weather.exception.GeocodingClientException;
import com.github.kramarenkoagnieszka.weather.exception.MissingParameterException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherApplicationException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;
import com.github.kramarenkoagnieszka.weather.model.CityRequest;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;

import java.util.Collections;
import java.util.Map;

public class WeatherQueryHandler implements RequestHandler<Map<String, Object>, WeatherResponse> {

  private static final ApplicationContext APP_CONTEXT = new ApplicationContext();

  @Override
  public WeatherResponse handleRequest(Map<String, Object> input, Context awsContext) {
    validateInput(input);

    return executeSafely(awsContext, () -> {
      Map<String, String> queryParams = extractQueryParams(input);
      String cityName = queryParams.get("city");

      CityRequest cityRequest = new CityRequest(cityName);
      return APP_CONTEXT.getWeatherService().getWeather(cityRequest);
    });
  }

  private void validateInput(Map<String, Object> input) {
    if (input == null) {
      throw new MissingParameterException("Input cannot be null");
    }
    Map<String, String> queryParams = (Map<String, String>) input.get("queryStringParameters");

    if (queryParams == null ||
        !queryParams.containsKey("city") ||
        queryParams.get("city") == null ||
        queryParams.get("city").isBlank()) {
      throw new MissingParameterException("Missing required GET query parameter: 'city'");
    }
  }

  private Map<String, String> extractQueryParams(Map<String, Object> input) {
    Object raw = input.get("queryStringParameters");
    if (!(raw instanceof Map)) {
      return Collections.emptyMap();
    }
    return (Map<String, String>) raw;
  }


  private WeatherResponse executeSafely(Context context, WeatherAction action) {
    try {
      context.getLogger().log("Processing weather request...");
      return action.execute();
    } catch (MissingParameterException e) {
      context.getLogger().log("Validation error: " + e.getMessage());
      throw new WeatherApplicationException("400 Bad Request: " + e.getMessage(), e);
    } catch (GeocodingClientException e) {
      context.getLogger().log("Geocoding service error: " + e.getMessage());
      throw new WeatherApplicationException("500 Geocoding Service Error: " + e.getMessage(), e);
    } catch (WeatherClientException e) {
      context.getLogger().log("Weather service error: " + e.getMessage());
      throw new WeatherApplicationException("500 Weather Service Error: " + e.getMessage(), e);
    } catch (WeatherApplicationException e) {
      context.getLogger().log("Service error: " + e.getMessage());
      throw new WeatherApplicationException("500 Service Error: " + e.getMessage(), e);
    } catch (Exception e) {
      context.getLogger().log("Fatal error: " + e.getMessage());
      throw new WeatherApplicationException("500 Internal Server Error", e);
    }
  }

  @FunctionalInterface
  private interface WeatherAction {

    WeatherResponse execute() throws Exception;
  }
}
