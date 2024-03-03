package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;

import java.util.List;

public interface IncidentService {
    IncidentEntityDto saveIncident(IncidentEntityDto incidentDto);
    List<IncidentEntityDto> findAllIncidents();

    List<IncidentEntityDto> searchIncidentsByType(String type);
    List<IncidentEntityDto> searchIncidents(IncidentSearchQueryDto queryDto);
}
