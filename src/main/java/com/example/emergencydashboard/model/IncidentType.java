package com.example.emergencydashboard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum IncidentType {
    FIRE("fire"),
    MEDICAL("medical"),
    POLICE("police"),
    NONE("");

    private final String type;

    @JsonValue
    public String getType() {
        return type;
    }

    @JsonCreator
    public static IncidentType forValue(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return NONE;
        }
        return Arrays.stream(IncidentType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid incident type: " + value));
    }

}
