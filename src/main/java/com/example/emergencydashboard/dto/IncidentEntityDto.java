package com.example.emergencydashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentEntityDto {

    private Integer id;
    private String incidentType;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
    private String severityLevel;

}
