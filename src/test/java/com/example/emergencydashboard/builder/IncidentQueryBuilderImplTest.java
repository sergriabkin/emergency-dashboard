package com.example.emergencydashboard.builder;

import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import org.assertj.core.api.Assertions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class IncidentQueryBuilderImplTest {

    private final IncidentQueryBuilderImpl queryBuilder = new IncidentQueryBuilderImpl();

    @Test
    void whenBuildQueryWithIncidentType_thenShouldContainMatchQuery() {
        // Arrange
        var queryDto = IncidentSearchQueryDto.builder()
                .incidentType("fire")
                .build();

        // Act
        BoolQueryBuilder boolQueryBuilder = queryBuilder.getBoolQueryBuilder(queryDto);

        // Assert
        Assertions.assertThat(boolQueryBuilder.should().size()).isEqualTo(1);
        Assertions.assertThat(boolQueryBuilder.toString()).isEqualTo(getTypeShouldJson());
    }

    private String getTypeShouldJson() {
        return "{\n" +
                "  \"bool\" : {\n" +
                "    \"should\" : [\n" +
                "      {\n" +
                "        \"match\" : {\n" +
                "          \"incidentType\" : {\n" +
                "            \"query\" : \"fire\",\n" +
                "            \"operator\" : \"OR\",\n" +
                "            \"prefix_length\" : 0,\n" +
                "            \"max_expansions\" : 50,\n" +
                "            \"fuzzy_transpositions\" : true,\n" +
                "            \"lenient\" : false,\n" +
                "            \"zero_terms_query\" : \"NONE\",\n" +
                "            \"auto_generate_synonyms_phrase_query\" : true,\n" +
                "            \"boost\" : 3.0\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"adjust_pure_negative\" : true,\n" +
                "    \"boost\" : 1.0\n" +
                "  }\n" +
                "}";
    }

    @Test
    void whenBuildQueryWithLocation_thenShouldContainGeoDistanceQuery() {
        // Arrange
        var queryDto = IncidentSearchQueryDto.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .build();

        // Act
        BoolQueryBuilder boolQueryBuilder = queryBuilder.getBoolQueryBuilder(queryDto);

        // Assert
        Assertions.assertThat(boolQueryBuilder.must().size()).isGreaterThan(0);
        Assertions.assertThat(boolQueryBuilder.toString()).isEqualTo(getGeoDistanceLocationJson());
    }

    private String getGeoDistanceLocationJson() {
        return "{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [\n" +
                "      {\n" +
                "        \"geo_distance\" : {\n" +
                "          \"location\" : [\n" +
                "            -74.006,\n" +
                "            40.7128\n" +
                "          ],\n" +
                "          \"distance\" : 10000.0,\n" +
                "          \"distance_type\" : \"arc\",\n" +
                "          \"validation_method\" : \"STRICT\",\n" +
                "          \"ignore_unmapped\" : false,\n" +
                "          \"boost\" : 1.0\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"adjust_pure_negative\" : true,\n" +
                "    \"boost\" : 1.0\n" +
                "  }\n" +
                "}";
    }

    @Test
    void whenBuildQueryWithTimestamp_thenShouldContainRangeQuery() {
        // Arrange

        LocalDateTime dateTime = LocalDateTime.parse("2024-03-01T11:52:16", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        var queryDto = IncidentSearchQueryDto.builder()
                .timestamp(dateTime)
                .build();

        // Act
        BoolQueryBuilder boolQueryBuilder = queryBuilder.getBoolQueryBuilder(queryDto);

        // Assert
        Assertions.assertThat(boolQueryBuilder.must().size()).isGreaterThan(0);
        Assertions.assertThat(boolQueryBuilder.toString()).isEqualTo(getTimestampRangeJson());
    }

    private String getTimestampRangeJson() {
        return "{\n" +
                "  \"bool\" : {\n" +
                "    \"must\" : [\n" +
                "      {\n" +
                "        \"range\" : {\n" +
                "          \"timestamp\" : {\n" +
                "            \"from\" : \"2024-03-01T10:52:16.000Z\",\n" +
                "            \"to\" : \"2024-03-01T12:52:16.000Z\",\n" +
                "            \"include_lower\" : true,\n" +
                "            \"include_upper\" : true,\n" +
                "            \"boost\" : 1.0\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"adjust_pure_negative\" : true,\n" +
                "    \"boost\" : 1.0\n" +
                "  }\n" +
                "}";
    }

    @Test
    void whenBuildQuery_thenNativeSearchQueryReturned() {
        // Arrange
        var queryDto = IncidentSearchQueryDto.builder().build();

        // Act
        var query = queryBuilder.buildQuery(queryDto);

        // Assert
        Assertions.assertThat(query)
                .isInstanceOf(NativeSearchQuery.class);
    }

}
