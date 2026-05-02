package com.github.kramarenkoagnieszka.weather.client;

import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.CityRequest;

public interface GeocodingClient {

  City getCity(CityRequest request);
}
