package com.github.kramarenkoagnieszka.weather.client;

import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;

public interface WeatherClient {
    double getTemperature(double lat, double lon) throws WeatherClientException;
}
