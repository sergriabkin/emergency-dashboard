package com.example.emergencydashboard.service;

import com.example.emergencydashboard.model.IncidentEntity;
import java.util.List;

public interface IncidentService {
    IncidentEntity saveIncident(IncidentEntity incidentEntity);
    List<IncidentEntity> findAllIncidents();
    List<IncidentEntity> searchIncidents(String query);
}
