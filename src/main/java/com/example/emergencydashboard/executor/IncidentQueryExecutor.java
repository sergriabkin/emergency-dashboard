package com.example.emergencydashboard.executor;

import com.example.emergencydashboard.model.IncidentDocument;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;

public interface IncidentQueryExecutor {
    SearchHits<IncidentDocument> executeQuery(Query searchQuery);
}
