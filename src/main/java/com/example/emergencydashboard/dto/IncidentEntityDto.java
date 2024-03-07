package com.example.emergencydashboard.dto;

import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentEntityDto {

    public static final String LONGITUDE_RANGE_MESSAGE = "Longitude must be between -180.0 and 180.0";
    public static final String LATITUDE_RANGE_MESSAGE = "Latitude must be between -90.0 and 90.0";
    public static final String LATITUDE_MIN = "-90.0";
    public static final String LATITUDE_MAX = "90.0";
    public static final String LONGITUDE_MIN = "-180.0";
    public static final String LONGITUDE_MAX = "180.0";

    private String id;
    private IncidentType incidentType;

    @DecimalMin(value = LATITUDE_MIN, message = LATITUDE_RANGE_MESSAGE)
    @DecimalMax(value = LATITUDE_MAX, message = LATITUDE_RANGE_MESSAGE)
    private Double latitude;

    @DecimalMin(value = LONGITUDE_MIN, message = LONGITUDE_RANGE_MESSAGE)
    @DecimalMax(value = LONGITUDE_MAX, message = LONGITUDE_RANGE_MESSAGE)
    private Double longitude;

    private LocalDateTime timestamp;
    private SeverityLevel severityLevel;

}
