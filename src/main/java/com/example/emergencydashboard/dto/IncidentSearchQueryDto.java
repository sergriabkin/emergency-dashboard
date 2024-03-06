package com.example.emergencydashboard.dto;

import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IncidentSearchQueryDto {
    private IncidentType incidentType;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private SeverityLevel severityLevel;
}
