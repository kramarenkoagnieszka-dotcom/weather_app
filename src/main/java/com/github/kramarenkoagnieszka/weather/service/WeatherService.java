package com.github.kramarenkoagnieszka.weather.service;

import com.github.kramarenkoagnieszka.weather.client.WeatherClient;
import com.github.kramarenkoagnieszka.weather.exception.WeatherApplicationException;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WeatherService {

    private final WeatherClient weatherClient;
    private final TemperatureClassifier classifier;

    public WeatherResponse getWeather(City city) throws WeatherApplicationException {
        try {
            double temp = weatherClient.getTemperature(city);

            return WeatherResponse.builder()
                    .city(city)
                    .temperature(temp)
                    .category(classifier.classify(temp))
                    .build();
        } catch (Exception e) {
            throw new WeatherApplicationException("Failed to fetch weather for " + city.getDisplayName(), e);
        }
    }
}
