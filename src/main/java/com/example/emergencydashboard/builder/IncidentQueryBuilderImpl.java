package com.example.emergencydashboard.builder;

import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Component
public class IncidentQueryBuilderImpl implements IncidentQueryBuilder {
    @Override
    public Query buildQuery(IncidentSearchQueryDto queryDto) {

        BoolQueryBuilder queryBuilder = getBoolQueryBuilder(queryDto);

        return new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .build();
    }

    protected BoolQueryBuilder getBoolQueryBuilder(IncidentSearchQueryDto queryDto) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (queryDto.getIncidentType() != null) {
            queryBuilder.should(
                    matchQuery("incidentType", queryDto.getIncidentType())
                            .boost(3.0f)
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
        return queryBuilder;
    }
}
