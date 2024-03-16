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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidentServiceImplTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String STRING_ID = "fd5ae13f-3c1c-49be-bda8-405b721be873";
    private static final UUID UUID_ID = UUID.fromString(STRING_ID);
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
        IncidentEntityDto dto = new IncidentEntityDto(STRING_ID, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);
        IncidentEntity entity = new IncidentEntity(UUID_ID, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);

        when(jpaRepository.save(any(IncidentEntity.class))).thenReturn(entity);
        when(searchRepository.save(any(IncidentDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncidentEntityDto result = service.saveIncident(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getIncidentType()).isEqualTo(dto.getIncidentType());
    }

    @Test
    void findAllIncidents() {
        IncidentEntity entity = new IncidentEntity(UUID_ID, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);
        List<IncidentEntity> entities = Collections.singletonList(entity);

        when(jpaRepository.findAll()).thenReturn(entities);

        List<IncidentEntityDto> result = service.findAllIncidents();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getIncidentType()).isEqualTo(entity.getIncidentType());
    }

    @Test
    void updateIncident() {
        IncidentEntityDto dtoToUpdate = new IncidentEntityDto(STRING_ID, IncidentType.MEDICAL, 41.712776, -73.005974, NOW, SeverityLevel.HIGH);
        IncidentEntity updatedEntity = new IncidentEntity(UUID_ID, IncidentType.MEDICAL, 41.712776, -73.005974, NOW, SeverityLevel.HIGH);

        when(jpaRepository.existsById(UUID_ID)).thenReturn(true);
        when(jpaRepository.save(any(IncidentEntity.class))).thenReturn(updatedEntity);
        when(searchRepository.save(any(IncidentDocument.class))).thenAnswer(invocation -> invocation.getArgument(0));

        IncidentEntityDto result = service.updateIncident(STRING_ID, dtoToUpdate);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(STRING_ID);
        assertThat(result.getIncidentType()).isEqualTo(dtoToUpdate.getIncidentType());
        verify(jpaRepository).save(any(IncidentEntity.class));
        verify(searchRepository).save(any(IncidentDocument.class));
    }

    @Test
    void updateIncident_NotFound() {
        String id = STRING_ID;
        IncidentEntityDto dtoToUpdate = new IncidentEntityDto(id, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);

        when(jpaRepository.existsById(UUID_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.updateIncident(id, dtoToUpdate));

        verify(jpaRepository, never()).save(any(IncidentEntity.class));
        verify(searchRepository, never()).save(any(IncidentDocument.class));
    }

    @Test
    void deleteIncident() {
        String id = STRING_ID;

        when(jpaRepository.existsById(UUID_ID)).thenReturn(true);

        service.deleteIncident(id);

        verify(jpaRepository).deleteById(UUID_ID);
        verify(searchRepository).deleteById(id);
    }

    @Test
    void deleteIncident_NotFound() {
        String id = STRING_ID;

        when(jpaRepository.existsById(UUID_ID)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deleteIncident(id));

        verify(jpaRepository, never()).deleteById(UUID_ID);
        verify(searchRepository, never()).deleteById(id);
    }

}
