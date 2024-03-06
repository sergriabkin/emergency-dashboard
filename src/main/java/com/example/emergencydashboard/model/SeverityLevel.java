package com.example.emergencydashboard.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public enum SeverityLevel {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    URGENT("urgent"),
    NONE("");

    private final String level;

    @JsonValue
    public String getLevel() {
        return level;
    }

    @JsonCreator
    public static SeverityLevel forValue(String value) {
        if (Objects.isNull(value) || value.isBlank()) {
            return NONE;
        }
        return Arrays.stream(SeverityLevel.values())
                .filter(level -> level.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid incident type: " + value));
    }
}
