package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private IncidentJpaRepository jpaRepository;

    @Mock
    private IncidentSearchRepository searchRepository;

    @Spy
    private IncidentMapper mapper = IncidentMapper.INSTANCE;

    @InjectMocks
    private IncidentServiceImpl service;

    @Test
    void saveIncident() {
        IncidentEntityDto dto = new IncidentEntityDto("1", IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);
        IncidentEntity entity = new IncidentEntity("1", IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);

        when(jpaRepository.save(any(IncidentEntity.class))).thenReturn(entity);
        when(searchRepository.save(any(IncidentDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncidentEntityDto result = service.saveIncident(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getIncidentType()).isEqualTo(dto.getIncidentType());
    }

    @Test
    void findAllIncidents() {
        IncidentEntity entity = new IncidentEntity("1", IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);
        List<IncidentEntity> entities = Collections.singletonList(entity);

        when(jpaRepository.findAll()).thenReturn(entities);

        List<IncidentEntityDto> result = service.findAllIncidents();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIncidentType()).isEqualTo(entity.getIncidentType());
    }

    @Test
    void updateIncident() {
        String id = "1";
        IncidentEntityDto dtoToUpdate = new IncidentEntityDto(id, IncidentType.MEDICAL, 41.712776, -73.005974, NOW, SeverityLevel.HIGH);
        IncidentEntity updatedEntity = new IncidentEntity(id, IncidentType.MEDICAL, 41.712776, -73.005974, NOW, SeverityLevel.HIGH);

        when(jpaRepository.existsById(id)).thenReturn(true);
        when(jpaRepository.save(any(IncidentEntity.class))).thenReturn(updatedEntity);
        when(searchRepository.save(any(IncidentDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncidentEntityDto result = service.updateIncident(id, dtoToUpdate);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIncidentType()).isEqualTo(dtoToUpdate.getIncidentType());
        verify(jpaRepository).save(any(IncidentEntity.class));
        verify(searchRepository).save(any(IncidentDocument.class));
    }

    @Test
    void updateIncident_NotFound() {
        String id = "2";
        IncidentEntityDto dtoToUpdate = new IncidentEntityDto(id, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);

        when(jpaRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.updateIncident(id, dtoToUpdate));

        verify(jpaRepository, never()).save(any(IncidentEntity.class));
        verify(searchRepository, never()).save(any(IncidentDocument.class));
    }

    @Test
    void deleteIncident() {
        String id = "1";

        when(jpaRepository.existsById(id)).thenReturn(true);

        service.deleteIncident(id);

        verify(jpaRepository).deleteById(id);
        verify(searchRepository).deleteById(id);
    }

    @Test
    void deleteIncident_NotFound() {
        String id = "2";

        when(jpaRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deleteIncident(id));

        verify(jpaRepository, never()).deleteById(id);
        verify(searchRepository, never()).deleteById(id);
    }

}
