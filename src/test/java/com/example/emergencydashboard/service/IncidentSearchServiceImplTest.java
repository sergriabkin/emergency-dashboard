package com.example.emergencydashboard.service;

import com.example.emergencydashboard.builder.IncidentQueryBuilder;
import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.executor.IncidentQueryExecutor;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncidentSearchServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    @Mock
    private IncidentSearchRepository searchRepository;

    @Mock
    private IncidentQueryBuilder queryBuilder;

    @Mock
    private IncidentQueryExecutor queryExecutor;

    @Spy
    private IncidentMapper mapper = IncidentMapper.INSTANCE;

    @InjectMocks
    private IncidentSearchServiceImpl service;


    @Test
    void searchIncidentsByType() {
        IncidentType query = IncidentType.FIRE;

        IncidentDocument document1 = new IncidentDocument();
        document1.setId("1");
        document1.setIncidentType(query);
        document1.setLocation(new GeoPoint(-74.0060, 40.7128));
        document1.setTimestamp(NOW);
        document1.setSeverityLevel(SeverityLevel.MEDIUM);

        IncidentDocument document2 = new IncidentDocument();
        document2.setId("2");
        document2.setIncidentType(query);
        document2.setLocation(new GeoPoint(-18.2437, 34.0522));
        document2.setTimestamp(NOW);
        document2.setSeverityLevel(SeverityLevel.HIGH);

        List<IncidentDocument> foundDocuments = List.of(document1, document2);

        when(searchRepository.findByIncidentType(anyString())).thenReturn(foundDocuments);

        List<IncidentEntityDto> result = service.searchIncidentsByType(query);

        assertThat(result).hasSize(foundDocuments.size());
        assertThat(result.get(0).getIncidentType()).isEqualTo(query);
        assertThat(result.get(1).getIncidentType()).isEqualTo(query);

        assertThat(result.get(0).getId()).isEqualTo(document1.getId());
        assertThat(result.get(1).getId()).isEqualTo(document2.getId());
    }


    @Test
    void searchIncidents() {

        var queryDto = mock(IncidentSearchQueryDto.class);
        var query = mock(Query.class);
        when(queryBuilder.buildQuery(queryDto)).thenReturn(query);

        var searchHits = mock(SearchHits.class);
        var hit = mock(SearchHit.class);
        when(searchHits.getSearchHits()).thenReturn(List.of(hit));
        when(queryExecutor.executeQuery(query)).thenReturn(searchHits);

        IncidentDocument document = new IncidentDocument();
        document.setId("1");
        document.setIncidentType(IncidentType.FIRE);
        document.setLocation(new GeoPoint(-74.0060, 40.7128));
        document.setTimestamp(NOW);
        document.setSeverityLevel(SeverityLevel.MEDIUM);

        when(hit.getContent()).thenReturn(document);

        List<IncidentEntityDto> result = service.searchIncidents(queryDto);

        assertThat(result).hasSize(1);
        assertThat(result.get(0))
                .matches(entityDto -> entityDto.getId().equals(document.getId()))
                .matches(entityDto -> entityDto.getIncidentType().equals(document.getIncidentType()))
                .matches(entityDto -> entityDto.getLatitude().equals(document.getLocation().getLat()))
                .matches(entityDto -> entityDto.getLongitude().equals(document.getLocation().getLon()))
                .matches(entityDto -> entityDto.getTimestamp().equals(document.getTimestamp()))
                .matches(entityDto -> entityDto.getSeverityLevel().equals(document.getSeverityLevel()));
    }

}
