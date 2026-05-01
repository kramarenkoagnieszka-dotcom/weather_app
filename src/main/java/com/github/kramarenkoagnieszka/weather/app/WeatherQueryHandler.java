package com.github.kramarenkoagnieszka.weather.app;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.github.kramarenkoagnieszka.weather.exception.WeatherApplicationException;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;
import com.github.kramarenkoagnieszka.weather.service.WeatherService;

import java.util.Map;

public class WeatherQueryHandler implements RequestHandler<Map<String, Object>, WeatherResponse> {

    private static final ApplicationContext APP_CONTEXT = new ApplicationContext();

    @Override
    public WeatherResponse handleRequest(Map<String, Object> input, Context AWSContext) {
        AWSContext.getLogger().log("Processing weather request...");

        try {
            String cityName = input.getOrDefault("city", "WROCLAW").toString().toUpperCase();
            City city = City.valueOf(cityName);

            WeatherService weatherService = APP_CONTEXT.getWeatherService();
            return weatherService.getWeather(city);

        } catch (IllegalArgumentException e) {
            AWSContext.getLogger().log("Invalid city: " + e.getMessage());
            throw new RuntimeException("Error: City not supported. Supported: WROCLAW, WARSAW.");
        } catch (WeatherApplicationException e) {
            AWSContext.getLogger().log("Weather app error: " + e.getMessage());
            throw new RuntimeException("Service Error: Could not process weather data.", e);
        } catch (Exception e) {
            // Tutaj cała reszta, której nie przewidzieliśmy
            AWSContext.getLogger().log("Unexpected system error: " + e.getMessage());
            throw new RuntimeException("Internal Server Error.", e);
        }
    }
}
