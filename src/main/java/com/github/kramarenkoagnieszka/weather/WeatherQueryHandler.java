package com.github.kramarenkoagnieszka.weather;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;

import java.util.Map;

public class WeatherQueryHandler implements RequestHandler<Map<String, Object>, WeatherResponse> {

    private static final ApplicationContext APP_CONTEXT = new ApplicationContext();

    @Override
    public WeatherResponse handleRequest(Map<String, Object> input, Context context) {
        context.getLogger().log("Processing weather request for city...");

        try {
            String cityName = input.getOrDefault("city", "WROCLAW").toString().toUpperCase();

            City city = City.valueOf(cityName);

            WeatherService weatherService = APP_CONTEXT.getWeatherService();

            return weatherService.getWeather(city);

        } catch (IllegalArgumentException e) {
            context.getLogger().log("Unsupported city provided in request.");
            throw new RuntimeException("Error: City not supported. Supported: WROCLAW, WARSAW.");
        } catch (Exception e) {
            context.getLogger().log("Unexpected error: " + e.getMessage());
            throw new RuntimeException("Internal Server Error occurred while fetching weather.");
        }
    }
}
