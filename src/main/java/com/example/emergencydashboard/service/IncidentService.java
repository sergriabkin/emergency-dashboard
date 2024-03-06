package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;

import java.util.List;

public interface IncidentService {
    IncidentEntityDto saveIncident(IncidentEntityDto incidentDto);
    List<IncidentEntityDto> findAllIncidents();

    List<IncidentEntityDto> searchIncidentsByType(IncidentType type);
    List<IncidentEntityDto> searchIncidents(IncidentSearchQueryDto queryDto);

    IncidentEntityDto updateIncident(String id, IncidentEntityDto incidentDto);

    void deleteIncident(String id);
}
