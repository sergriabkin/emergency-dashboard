package com.example.emergencydashboard.builder;

import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import org.springframework.data.elasticsearch.core.query.Query;

public interface IncidentQueryBuilder {
    Query buildQuery(IncidentSearchQueryDto queryDto);
}
