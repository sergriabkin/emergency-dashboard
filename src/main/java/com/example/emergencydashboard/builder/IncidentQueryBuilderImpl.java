package com.example.emergencydashboard.builder;

import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;
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

    private static final String SCORE_FIELD_NAME = "_score";
    private static final String INCIDENT_TYPE_FIELD_NAME = "incidentType";
    private static final String LOCATION_FIELD_NAME = "location";
    private static final String TIMESTAMP_FIELD_NAME = "timestamp";
    private static final int DISTANCE_PRECISION_KILOMETERS = 10;
    private static final int TIME_PRECISION_HOURS = 1;
    private static final float INCIDENT_TYPE_BOOST = 3.0f;

    @Override
    public Query buildQuery(IncidentSearchQueryDto queryDto) {

        BoolQueryBuilder queryBuilder = getBoolQueryBuilder(queryDto);

        return new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSort(SortBuilders.fieldSort(SCORE_FIELD_NAME).order(SortOrder.DESC))
                .build();
    }

    protected BoolQueryBuilder getBoolQueryBuilder(IncidentSearchQueryDto queryDto) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (queryDto.getIncidentType() != null && queryDto.getIncidentType() != IncidentType.NONE) {
            queryBuilder.should(
                    matchQuery(INCIDENT_TYPE_FIELD_NAME, queryDto.getIncidentType().getType())
                            .boost(INCIDENT_TYPE_BOOST)
            );
        }

        if (queryDto.getLatitude() != null && queryDto.getLongitude() != null) {
            queryBuilder.must(geoDistanceQuery(LOCATION_FIELD_NAME)
                    .point(queryDto.getLatitude(), queryDto.getLongitude())
                    .distance(DISTANCE_PRECISION_KILOMETERS, DistanceUnit.KILOMETERS));
        }

        if (queryDto.getTimestamp() != null) {
            queryBuilder.must(
                    rangeQuery(TIMESTAMP_FIELD_NAME)
                            .gte(queryDto.getTimestamp().minusHours(TIME_PRECISION_HOURS).toString())
                            .lte(queryDto.getTimestamp().plusHours(TIME_PRECISION_HOURS).toString())
            );
        }
        return queryBuilder;
    }
}
