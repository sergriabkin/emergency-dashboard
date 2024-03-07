package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;

import java.util.List;

public interface IncidentSearchService {
    List<IncidentEntityDto> searchIncidentsByType(IncidentType type);
    List<IncidentEntityDto> searchIncidents(IncidentSearchQueryDto queryDto);
}
