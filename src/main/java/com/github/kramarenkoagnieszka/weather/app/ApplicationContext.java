package com.github.kramarenkoagnieszka.weather.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kramarenkoagnieszka.weather.client.WeatherClient;
import com.github.kramarenkoagnieszka.weather.client.openmeteo.OpenMeteoClient;

import com.github.kramarenkoagnieszka.weather.service.TemperatureClassifier;
import com.github.kramarenkoagnieszka.weather.service.WeatherService;
import lombok.Getter;

import java.net.http.HttpClient;
import java.time.Duration;

@Getter
public class ApplicationContext {
    private static final long HTTP_CONNECT_TIMEOUT_SECONDS = 10L;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final WeatherClient weatherClient;
    private final TemperatureClassifier temperatureClassifier;
    private final WeatherService weatherService;

    public ApplicationContext() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(HTTP_CONNECT_TIMEOUT_SECONDS))
                .build();

        this.weatherClient = new OpenMeteoClient(httpClient, objectMapper);
        this.temperatureClassifier = new TemperatureClassifier();

        this.weatherService = new WeatherService(weatherClient, temperatureClassifier);
    }
}
