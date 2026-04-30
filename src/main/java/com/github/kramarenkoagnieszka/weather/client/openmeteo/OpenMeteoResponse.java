package com.github.kramarenkoagnieszka.weather.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoResponse {

    @JsonProperty("current")
    private CurrentWeather current;
}
