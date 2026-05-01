package com.github.kramarenkoagnieszka.weather.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.kramarenkoagnieszka.weather.exception.InvalidCityException;
import com.github.kramarenkoagnieszka.weather.exception.MissingParameterException;
import com.github.kramarenkoagnieszka.weather.exception.WeatherApplicationException;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;

import java.util.Map;

public class WeatherQueryHandler implements RequestHandler<Map<String, Object>, WeatherResponse> {

  private static final ApplicationContext APP_CONTEXT = new ApplicationContext();

  @Override
  public WeatherResponse handleRequest(Map<String, Object> input, Context awsContext) {
    awsContext.getLogger().log("Processing weather request...");

    try {
      if (input == null || !input.containsKey("city") || input.get("city") == null) {
        throw new MissingParameterException("Missing required parameter: 'city'");
      }

      String cityName = input.get("city").toString();

      City city = City.fromString(cityName)
          .orElseThrow(() -> new InvalidCityException("City not supported: " + cityName));

      return APP_CONTEXT.getWeatherService().getWeather(city);

    } catch (MissingParameterException | InvalidCityException e) {
      awsContext.getLogger().log("Validation error: " + e.getMessage());
      throw new RuntimeException("400 Bad Request: " + e.getMessage());
    } catch (WeatherApplicationException e) {
      awsContext.getLogger().log("Service error: " + e.getMessage());
      throw new RuntimeException("500 Service Error: " + e.getMessage());
    } catch (Exception e) {
      awsContext.getLogger().log("Fatal error: " + e.getMessage());
      throw new RuntimeException("500 Internal Server Error");
    }
  }
  }
