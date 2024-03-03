package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private IncidentJpaRepository jpaRepository;

    @Mock
    private IncidentSearchRepository searchRepository;

    @InjectMocks
    private IncidentServiceImpl service;

    @Test
    void saveIncident() {
        IncidentEntityDto dto = new IncidentEntityDto("1", "Fire", 40.712776, -74.005974, NOW, "High");
        IncidentEntity entity = new IncidentEntity("1", "Fire", 40.712776, -74.005974, NOW, "High");

        when(jpaRepository.save(any(IncidentEntity.class))).thenReturn(entity);
        when(searchRepository.save(any(IncidentDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncidentEntityDto result = service.saveIncident(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getIncidentType()).isEqualTo(dto.getIncidentType());
    }

    @Test
    void findAllIncidents() {
        IncidentEntity entity = new IncidentEntity("1", "Fire", 40.712776, -74.005974, NOW, "High");
        List<IncidentEntity> entities = Collections.singletonList(entity);

        when(jpaRepository.findAll()).thenReturn(entities);

        List<IncidentEntityDto> result = service.findAllIncidents();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIncidentType()).isEqualTo(entity.getIncidentType());
    }


    @Test
    void searchIncidentsByType() {
        String query = "Fire";

        IncidentDocument document1 = new IncidentDocument();
        document1.setId("1");
        document1.setIncidentType(query);
        document1.setLocation(new GeoPoint(-74.0060, 40.7128));
        document1.setTimestamp(NOW);
        document1.setSeverityLevel("Medium");

        IncidentDocument document2 = new IncidentDocument();
        document2.setId("2");
        document2.setIncidentType(query);
        document2.setLocation(new GeoPoint(-18.2437, 34.0522));
        document2.setTimestamp(NOW);
        document2.setSeverityLevel("High");

        List<IncidentDocument> foundDocuments = List.of(document1, document2);

        when(searchRepository.findByIncidentType(anyString())).thenReturn(foundDocuments);

        List<IncidentEntityDto> result = service.searchIncidentsByType(query);

        assertThat(result).hasSize(foundDocuments.size());
        assertThat(result.get(0).getIncidentType()).isEqualTo(query);
        assertThat(result.get(1).getIncidentType()).isEqualTo(query);

        assertThat(result.get(0).getId()).isEqualTo(document1.getId());
        assertThat(result.get(1).getId()).isEqualTo(document2.getId());
    }
}
