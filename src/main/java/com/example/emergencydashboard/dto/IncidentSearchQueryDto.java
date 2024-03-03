package com.example.emergencydashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IncidentSearchQueryDto {
    private String incidentType;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private String severityLevel;
}
