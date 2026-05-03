package com.github.kramarenkoagnieszka.weather.client;

import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherRequest;

public interface GeocodingClient {

  City getCity(WeatherRequest request);
}
