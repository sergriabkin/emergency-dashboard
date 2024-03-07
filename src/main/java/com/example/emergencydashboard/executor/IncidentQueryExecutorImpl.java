package com.example.emergencydashboard.executor;

import com.example.emergencydashboard.model.IncidentDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentQueryExecutorImpl implements IncidentQueryExecutor {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public SearchHits<IncidentDocument> executeQuery(Query searchQuery) {
        logQuery(searchQuery);
        SearchHits<IncidentDocument> searchHits = elasticsearchTemplate.search(searchQuery, IncidentDocument.class);
        logResults(searchHits);
        return searchHits;
    }

    private void logResults(SearchHits<IncidentDocument> searchHits) {
        log.info("Search hits found: {}", searchHits.getSearchHits());
    }

    private void logQuery(Query searchQuery) {
        if (searchQuery instanceof NativeSearchQuery nativeSearchQuery) {
            log.info("Searching with custom NativeSearchQuery: {}", nativeSearchQuery.getQuery());
        }
    }
}
