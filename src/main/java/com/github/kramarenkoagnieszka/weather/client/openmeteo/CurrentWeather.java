package com.github.kramarenkoagnieszka.weather.client.openmeteo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {

    @JsonProperty("temperature_2m")
    private double temperature;
}
