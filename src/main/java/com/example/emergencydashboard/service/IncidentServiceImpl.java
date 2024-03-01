package com.example.emergencydashboard.service;

import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentJpaRepository jpaRepository;
    private final IncidentSearchRepository searchRepository;

    @Override
    public IncidentEntity saveIncident(IncidentEntity incidentEntity) {

        incidentEntity = jpaRepository.save(incidentEntity);

        IncidentDocument incidentDocument = convertEntityToDocument(incidentEntity);
        searchRepository.save(incidentDocument);
        return incidentEntity;
    }

    @Override
    public List<IncidentEntity> findAllIncidents() {
        return jpaRepository.findAll();
    }

    @Override
    public List<IncidentEntity> searchIncidents(String query) {
        return searchRepository.findByIncidentType(query)
                .stream()
                .map(this::convertDocumentToEntity)
                .collect(Collectors.toList());
    }

    private IncidentDocument convertEntityToDocument(IncidentEntity entity) {
        var incident = new IncidentDocument();
        incident.setId(entity.getId());
        incident.setIncidentType(entity.getIncidentType());
        incident.setSeverityLevel(entity.getSeverityLevel());
        incident.setTimestamp(entity.getTimestamp());
        incident.setLatitude(entity.getLatitude());
        incident.setLongitude(entity.getLongitude());
        return incident;
    }

    private IncidentEntity convertDocumentToEntity(IncidentDocument document) {
        var incident = new IncidentEntity();
        incident.setId(document.getId());
        incident.setIncidentType(document.getIncidentType());
        incident.setSeverityLevel(document.getSeverityLevel());
        incident.setTimestamp(document.getTimestamp());
        incident.setLatitude(document.getLatitude());
        incident.setLongitude(document.getLongitude());
        return incident;
    }
}
