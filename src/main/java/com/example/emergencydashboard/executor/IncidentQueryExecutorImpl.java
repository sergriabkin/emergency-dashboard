package com.example.emergencydashboard.executor;

import com.example.emergencydashboard.model.IncidentDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentQueryExecutorImpl implements IncidentQueryExecutor {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public SearchHits<IncidentDocument> executeQuery(Query searchQuery) {
        log.info("Searching with customQuery: {}", searchQuery);
        SearchHits<IncidentDocument> searchHits = elasticsearchTemplate.search(searchQuery, IncidentDocument.class);
        log.info("Search hits found: {}", searchHits.getSearchHits());
        return searchHits;
    }
}
