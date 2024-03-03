package com.example.emergencydashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String incidentType;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private String severityLevel;

}

