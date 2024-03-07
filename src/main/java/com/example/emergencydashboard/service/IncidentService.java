package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;

import java.util.List;

public interface IncidentService {
    IncidentEntityDto saveIncident(IncidentEntityDto incidentDto);

    List<IncidentEntityDto> findAllIncidents();

    IncidentEntityDto updateIncident(String id, IncidentEntityDto incidentDto);

    void deleteIncident(String id);
}
