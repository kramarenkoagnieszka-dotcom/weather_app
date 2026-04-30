package com.github.kramarenkoagnieszka.weather;

import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;

public interface WeatherClient {
    double getTemperature(double lat, double lon) throws WeatherClientException;
}
