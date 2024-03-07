package com.example.emergencydashboard.executor;

import com.example.emergencydashboard.model.IncidentDocument;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentQueryExecutorImplTest {

    @Mock
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @InjectMocks
    private IncidentQueryExecutorImpl incidentQueryExecutor;

    @Test
    void whenExecuteQuery_thenShouldCallTemplateAndReturnSearchHits() {
        // Arrange
        var query = mock(NativeSearchQuery.class);
        when(query.getQuery()).thenReturn(QueryBuilders.matchAllQuery());
        var expectedDocument = mock(IncidentDocument.class);
        var expectedSearchHits = mock(SearchHits.class);
        when(expectedSearchHits.getTotalHits()).thenReturn(1L);
        when(expectedSearchHits.getSearchHits()).thenReturn(List.of(expectedDocument));
        when(elasticsearchRestTemplate.search(query, IncidentDocument.class)).thenReturn(expectedSearchHits);

        // Act
        SearchHits<IncidentDocument> searchHits = incidentQueryExecutor.executeQuery(query);

        // Assert
        assertThat(searchHits).isNotNull();
        assertThat(searchHits.getTotalHits()).isEqualTo(1L);
        verify(elasticsearchRestTemplate).search(query, IncidentDocument.class);
    }
}
