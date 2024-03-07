package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private static final String INCIDENT_NOT_FOUND_TEMPLATE = "Incident not found with id: ";

    private final IncidentJpaRepository jpaRepository;
    private final IncidentSearchRepository searchRepository;
    private final IncidentMapper mapper;

    @Transactional
    @Override
    public IncidentEntityDto saveIncident(IncidentEntityDto incidentDto) {
        var entity = mapper.dtoToEntity(incidentDto);
        var savedEntity = jpaRepository.save(entity);
        indexToElastic(savedEntity);
        return mapper.entityToDto(entity);
    }

    @Override
    public List<IncidentEntityDto> findAllIncidents() {
        List<IncidentEntity> entities = jpaRepository.findAll();
        return entities.stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Transactional
    @Override
    public IncidentEntityDto updateIncident(String id, IncidentEntityDto incidentDto) {
        if (!jpaRepository.existsById(id)) {
            throw new EntityNotFoundException(INCIDENT_NOT_FOUND_TEMPLATE + id);
        }

        var entityToUpdate = mapper.dtoToEntity(incidentDto);
        entityToUpdate.setId(id);

        var updatedEntity = jpaRepository.save(entityToUpdate);

        indexToElastic(updatedEntity);

        return mapper.entityToDto(updatedEntity);
    }

    private void indexToElastic(IncidentEntity updatedEntity) {
        var updatedDocument = mapper.entityToDocument(updatedEntity);
        searchRepository.save(updatedDocument);
    }

    @Transactional
    @Override
    public void deleteIncident(String id) {
        if (!jpaRepository.existsById(id)) {
            throw new EntityNotFoundException(INCIDENT_NOT_FOUND_TEMPLATE + id);
        }

        jpaRepository.deleteById(id);
        searchRepository.deleteById(id);
    }

}
