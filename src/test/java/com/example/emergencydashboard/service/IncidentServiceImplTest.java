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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceImplTest {

    @Mock
    private IncidentJpaRepository jpaRepository;

    @Mock
    private IncidentSearchRepository searchRepository;

    @InjectMocks
    private IncidentServiceImpl service;

    @Test
    void saveIncident() {
        IncidentEntityDto dto = new IncidentEntityDto("1", "Fire", 40.712776, -74.005974, LocalDateTime.now(), "High");
        IncidentEntity entity = new IncidentEntity("1", "Fire", 40.712776, -74.005974, LocalDateTime.now(), "High");

        when(jpaRepository.save(any(IncidentEntity.class))).thenReturn(entity);
        when(searchRepository.save(any(IncidentDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncidentEntityDto result = service.saveIncident(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getIncidentType()).isEqualTo(dto.getIncidentType());
    }

    @Test
    void findAllIncidents() {
        IncidentEntity entity = new IncidentEntity("1", "Fire", 40.712776, -74.005974, LocalDateTime.now(), "High");
        List<IncidentEntity> entities = Collections.singletonList(entity);

        when(jpaRepository.findAll()).thenReturn(entities);

        List<IncidentEntityDto> result = service.findAllIncidents();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIncidentType()).isEqualTo(entity.getIncidentType());
    }


    @Test
    void searchIncidents() {
        String query = "Fire";
        LocalDateTime now = LocalDateTime.now();
        IncidentDocument document1 = new IncidentDocument("1", query, 40.7128, -74.0060, now, "Medium");
        IncidentDocument document2 = new IncidentDocument("2", query, 34.0522, -118.2437, now, "High");
        List<IncidentDocument> foundDocuments = List.of(document1, document2);

        when(searchRepository.findByIncidentType(anyString())).thenReturn(foundDocuments);

        List<IncidentEntityDto> result = service.searchIncidents(query);

        assertThat(result).hasSize(foundDocuments.size());
        assertThat(result.get(0).getIncidentType()).isEqualTo(query);
        assertThat(result.get(1).getIncidentType()).isEqualTo(query);

        assertThat(result.get(0).getId()).isEqualTo(document1.getId());
        assertThat(result.get(1).getId()).isEqualTo(document2.getId());
    }
}
