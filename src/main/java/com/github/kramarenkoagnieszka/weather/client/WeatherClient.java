package com.github.kramarenkoagnieszka.weather.client;

import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;
import com.github.kramarenkoagnieszka.weather.model.City;

public interface WeatherClient {

  double getTemperature(City city) throws WeatherClientException;
}
