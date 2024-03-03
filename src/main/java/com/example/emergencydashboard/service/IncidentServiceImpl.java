package com.example.emergencydashboard.service;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.mapper.IncidentMapper;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.repository.jpa.IncidentJpaRepository;
import com.example.emergencydashboard.repository.search.IncidentSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private final IncidentJpaRepository jpaRepository;
    private final IncidentSearchRepository searchRepository;
    private final ElasticsearchRestTemplate elasticsearchTemplate;
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

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (queryDto.getIncidentType() != null) {
            queryBuilder.should(
                    matchQuery("incidentType", queryDto.getIncidentType()).boost(3.0f)
            );
        }

        if (queryDto.getLatitude() != null && queryDto.getLongitude() != null) {
            queryBuilder.must(geoDistanceQuery("location")
                    .point(queryDto.getLatitude(), queryDto.getLongitude())
                    .distance(10, DistanceUnit.KILOMETERS));
        }

        if (queryDto.getTimestamp() != null) {
            queryBuilder.must(
                    rangeQuery("timestamp")
                            .gte(queryDto.getTimestamp().minusHours(1))
                            .lte(queryDto.getTimestamp().plusHours(1))
            );
        }

        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .build();

        log.info("Searching with customQuery: {}", searchQuery);
        SearchHits<IncidentDocument> searchHits = elasticsearchTemplate.search(searchQuery, IncidentDocument.class);
        log.info("Search hits found: {}", searchHits.getSearchHits());

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(mapper::documentToDto)
                .collect(Collectors.toList());
    }

}
