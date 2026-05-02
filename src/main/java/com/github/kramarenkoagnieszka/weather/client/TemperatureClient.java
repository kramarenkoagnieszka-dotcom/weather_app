package com.github.kramarenkoagnieszka.weather.client;

import com.github.kramarenkoagnieszka.weather.model.City;

public interface TemperatureClient {

  double getTemperature(City city);
}
