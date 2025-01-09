package org.example.tunnel;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum City {
    NONE,
    A,
    B;

    @JsonCreator
    public static City fromValue(String value) {
        for (City city : City.values()) {
            if (city.name().equalsIgnoreCase(value)) {
                return city;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
