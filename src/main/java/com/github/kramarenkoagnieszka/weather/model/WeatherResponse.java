package com.github.kramarenkoagnieszka.weather;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherResponse {
    public static final String DEFAULT_UNIT = "Celsius";

    private City city;
    private double temperature;

    @Builder.Default
    private String unit = DEFAULT_UNIT;

    private TemperatureCategory category;
}
