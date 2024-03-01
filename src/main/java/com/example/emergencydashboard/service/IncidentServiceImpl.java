package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentJpaRepository jpaRepository;
    private final IncidentSearchRepository searchRepository;
    private final IncidentMapper mapper = IncidentMapper.INSTANCE;

    @Override
    public IncidentEntityDto saveIncident(IncidentEntityDto incidentDto) {
        IncidentEntity entity = mapper.dtoToEntity(incidentDto);
        entity = jpaRepository.save(entity);
        IncidentDocument document = mapper.entityToDocument(entity);
        searchRepository.save(document);
        return mapper.entityToDto(entity);
    }

    @Override
    public List<IncidentEntityDto> findAllIncidents() {
        List<IncidentEntity> entities = jpaRepository.findAll();
        return entities.stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public List<IncidentEntityDto> searchIncidents(String query) {
        return searchRepository.findByIncidentType(query)
                .stream()
                .map(mapper::documentToDto)
                .collect(Collectors.toList());
    }
}
