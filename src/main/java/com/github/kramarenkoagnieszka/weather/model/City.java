package com.github.kramarenkoagnieszka.weather.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum City {
    WROCLAW("Wroclaw", 51.10, 17.03);

    private final String displayName;
    private final double latitude;
    private final double longitude;
}
