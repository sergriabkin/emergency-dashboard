package com.example.emergencydashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "incidents")
public class IncidentDocument {

    @Id
    private String id;

    private IncidentType incidentType;

    @Field(type = FieldType.Object)
    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Date, format = DateFormat.date_time_no_millis, pattern = "uuuu-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private SeverityLevel severityLevel;

}
