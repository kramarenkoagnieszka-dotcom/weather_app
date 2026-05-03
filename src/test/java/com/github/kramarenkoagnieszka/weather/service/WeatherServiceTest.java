package com.github.kramarenkoagnieszka.weather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.kramarenkoagnieszka.weather.client.GeocodingClient;
import com.github.kramarenkoagnieszka.weather.client.TemperatureClient;
import com.github.kramarenkoagnieszka.weather.model.City;
import com.github.kramarenkoagnieszka.weather.model.WeatherRequest;
import com.github.kramarenkoagnieszka.weather.model.WeatherResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

  @Mock
  private GeocodingClient geocodingClient;

  @Mock
  private TemperatureClient temperatureClient;
  private TemperatureClassifier classifier = new TemperatureClassifier();

  private WeatherService weatherService;

  @BeforeEach
  void setUp() {
    weatherService = new WeatherService(geocodingClient, temperatureClient, classifier);
  }

  @Test
  @DisplayName("Should return WARM category for 25 degrees")
  void shouldReturnWarmCategory() {
    WeatherRequest request = new WeatherRequest("Wroclaw");
    City city = new City("Wroclaw", 51.1, 17.0);

    when(geocodingClient.getCity(request)).thenReturn(city);
    when(temperatureClient.getTemperature(city)).thenReturn(25.0);

    WeatherResponse response = weatherService.getWeather(request);

    assertThat(response.getCategory()).isEqualTo("WARM"); // Bo 25 < 30.0 (UpperThreshold WARM)
    assertThat(response.getTemperature()).isEqualTo(25.0);
  }

  @Test
  @DisplayName("Should return FREEZING for sub-zero temperatures")
  void shouldReturnFreezing() {
    WeatherRequest request = new WeatherRequest("Kugaaruk");
    City city = new City("Kugaaruk", 68.5, -89.8);

    when(geocodingClient.getCity(request)).thenReturn(city);
    when(temperatureClient.getTemperature(city)).thenReturn(-15.0);

    WeatherResponse response = weatherService.getWeather(request);

    assertThat(response.getCategory()).isEqualTo("FREEZING");
  }
}
