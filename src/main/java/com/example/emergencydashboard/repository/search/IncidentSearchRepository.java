package com.example.emergencydashboard.repository.search;

import com.example.emergencydashboard.model.IncidentDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface IncidentSearchRepository extends ElasticsearchRepository<IncidentDocument, Integer> {
    List<IncidentDocument> findByIncidentType(String incidentType);
}
