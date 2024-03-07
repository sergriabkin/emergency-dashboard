package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IncidentServiceTransactionsRollbackTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    @Autowired
    private IncidentService incidentService;

    @MockBean
    private IncidentJpaRepository incidentJpaRepository;

    @MockBean
    private IncidentSearchRepository incidentSearchRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    void saveIncident_ShouldRollbackTransaction_OnElasticsearchFailure() {
        IncidentEntity mockEntity = new IncidentEntity();
        mockEntity.setId("1");
        mockEntity.setIncidentType(IncidentType.FIRE);
        mockEntity.setSeverityLevel(SeverityLevel.HIGH);
        mockEntity.setLatitude(40.712776);
        mockEntity.setLongitude(-74.005974);

        IncidentEntityDto dto = new IncidentEntityDto(null, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);

        when(incidentJpaRepository.save(any(IncidentEntity.class))).thenReturn(mockEntity);

        doThrow(new RuntimeException("Simulated Elasticsearch failure")).when(incidentSearchRepository).save(any(IncidentDocument.class));

        assertThrows(RuntimeException.class, () -> incidentService.saveIncident(dto));

        entityManager.flush();
        entityManager.clear();

        boolean incidentExistsInJPA = incidentJpaRepository.findById("1").isPresent();
        assertFalse(incidentExistsInJPA);

        verify(incidentSearchRepository, times(1)).save(any(IncidentDocument.class));
    }

    @Test
    @Transactional
    void updateIncident_ShouldRollbackTransaction_OnElasticsearchFailure() {
        String existingId = "1";
        IncidentEntity existingEntity = new IncidentEntity(existingId, IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.HIGH);

        IncidentEntityDto updateDto = new IncidentEntityDto(existingId, IncidentType.MEDICAL, 41.712776, -73.005974, NOW, SeverityLevel.MEDIUM);

        when(incidentJpaRepository.existsById(existingId)).thenReturn(true);
        when(incidentJpaRepository.save(any(IncidentEntity.class))).thenReturn(existingEntity);
        doThrow(new RuntimeException("Simulated Elasticsearch failure")).when(incidentSearchRepository).save(any(IncidentDocument.class));

        assertThrows(RuntimeException.class, () -> incidentService.updateIncident(existingId, updateDto));

        entityManager.flush();
        entityManager.clear();

        verify(incidentJpaRepository, times(1)).save(any(IncidentEntity.class));
        verify(incidentSearchRepository, times(1)).save(any(IncidentDocument.class));
    }

    @Test
    @Transactional
    void deleteIncident_ShouldRollbackTransaction_OnElasticsearchFailure() {
        String existingId = "1";

        when(incidentJpaRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(incidentJpaRepository).deleteById(existingId);
        doThrow(new RuntimeException("Simulated Elasticsearch failure")).when(incidentSearchRepository).deleteById(existingId);

        assertThrows(RuntimeException.class, () -> incidentService.deleteIncident(existingId));

        entityManager.flush();
        entityManager.clear();

        boolean incidentExistsInJPA = incidentJpaRepository.existsById(existingId);
        assertTrue(incidentExistsInJPA, "Incident should still exist in JPA repository after rollback");

        verify(incidentSearchRepository, times(1)).deleteById(existingId);
    }

}
