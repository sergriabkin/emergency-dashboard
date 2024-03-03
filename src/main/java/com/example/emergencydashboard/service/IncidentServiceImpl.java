package com.example.emergencydashboard.service;

import com.example.emergencydashboard.builder.IncidentQueryBuilder;
import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.executor.IncidentQueryExecutor;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private final IncidentJpaRepository jpaRepository;
    private final IncidentSearchRepository searchRepository;
    private final IncidentQueryBuilder queryBuilder;
    private final IncidentQueryExecutor queryExecutor;

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
    public List<IncidentEntityDto> searchIncidentsByType(String type) {
        return searchRepository.findByIncidentType(type)
                .stream()
                .map(mapper::documentToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<IncidentEntityDto> searchIncidents(IncidentSearchQueryDto queryDto) {

        Query searchQuery = queryBuilder.buildQuery(queryDto);

        SearchHits<IncidentDocument> searchHits = queryExecutor.executeQuery(searchQuery);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(mapper::documentToDto)
                .collect(Collectors.toList());
    }

}
