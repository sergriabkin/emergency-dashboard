package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import java.util.List;

public interface IncidentService {
    IncidentEntityDto saveIncident(IncidentEntityDto incidentEntity);
    List<IncidentEntityDto> findAllIncidents();
    List<IncidentEntityDto> searchIncidents(String query);
}
