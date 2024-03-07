package com.example.emergencydashboard.service;

import com.example.emergencydashboard.builder.IncidentQueryBuilder;
import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.executor.IncidentQueryExecutor;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentSearchServiceImpl implements IncidentSearchService {

    private final IncidentSearchRepository searchRepository;
    private final IncidentQueryBuilder queryBuilder;
    private final IncidentQueryExecutor queryExecutor;
    private final IncidentMapper mapper;

    @Override
    public List<IncidentEntityDto> searchIncidentsByType(IncidentType type) {
        return searchRepository.findByIncidentType(type.getType())
                .stream()
                .map(mapper::documentToDto)
                .toList();
    }

    @Override
    public List<IncidentEntityDto> searchIncidents(IncidentSearchQueryDto queryDto) {
        var searchQuery = queryBuilder.buildQuery(queryDto);
        var searchHits = queryExecutor.executeQuery(searchQuery);
        return mapHitsToDto(searchHits);
    }

    private List<IncidentEntityDto> mapHitsToDto(SearchHits<IncidentDocument> searchHits) {
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(mapper::documentToDto)
                .toList();
    }

}
