package com.github.kramarenkoagnieszka.weather.client.openmeteo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kramarenkoagnieszka.weather.client.WeatherClient;
import com.github.kramarenkoagnieszka.weather.exception.WeatherClientException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenMeteoClient implements WeatherClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenMeteoClient(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public double getTemperature(double lat, double lon) throws WeatherClientException {
        // Budujemy URL zgodnie z tym, co sprawdziłaś w przeglądarce
        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&current=temperature_2m",
                lat, lon);

        try {
            // 1. Przygotowanie zapytania
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // 2. Wysłanie zapytania i odebranie odpowiedzi jako String
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Sprawdzamy, czy serwer nie zwrócił błędu (np. 404 lub 500)
            if (response.statusCode() != 200) {
                throw new WeatherClientException("Open-Meteo returned error code: " + response.statusCode(), null);
            }

            // 3. Mapowanie JSON na Twoje klasy DTO
            OpenMeteoResponse data = objectMapper.readValue(response.body(), OpenMeteoResponse.class);

            // 4. Wyciągnięcie temperatury z "pudełek"
            if (data.getCurrent() == null) {
                throw new WeatherClientException("Invalid response: 'current' data is missing", null);
            }

            return data.getCurrent().getTemperature();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WeatherClientException("Weather request was interrupted", e);
        } catch (Exception e) {
            throw new WeatherClientException("Unexpected error while fetching weather data", e);
        }
    }
}
