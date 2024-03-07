package com.example.emergencydashboard.builder;

import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;
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
                .incidentType(IncidentType.FIRE)
                .build();

        // Act
        BoolQueryBuilder boolQueryBuilder = queryBuilder.getBoolQueryBuilder(queryDto);

        // Assert
        Assertions.assertThat(boolQueryBuilder.should()).hasSize(1);
        Assertions.assertThat(boolQueryBuilder).hasToString(getTypeShouldJson());
    }

    private String getTypeShouldJson() {
        return """
                {
                  "bool" : {
                    "should" : [
                      {
                        "match" : {
                          "incidentType" : {
                            "query" : "fire",
                            "operator" : "OR",
                            "prefix_length" : 0,
                            "max_expansions" : 50,
                            "fuzzy_transpositions" : true,
                            "lenient" : false,
                            "zero_terms_query" : "NONE",
                            "auto_generate_synonyms_phrase_query" : true,
                            "boost" : 3.0
                          }
                        }
                      }
                    ],
                    "adjust_pure_negative" : true,
                    "boost" : 1.0
                  }
                }""";
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
        Assertions.assertThat(boolQueryBuilder.must()).isNotEmpty();
        Assertions.assertThat(boolQueryBuilder).hasToString(getGeoDistanceLocationJson());
    }

    private String getGeoDistanceLocationJson() {
        return """
                {
                  "bool" : {
                    "must" : [
                      {
                        "geo_distance" : {
                          "location" : [
                            -74.006,
                            40.7128
                          ],
                          "distance" : 10000.0,
                          "distance_type" : "arc",
                          "validation_method" : "STRICT",
                          "ignore_unmapped" : false,
                          "boost" : 1.0
                        }
                      }
                    ],
                    "adjust_pure_negative" : true,
                    "boost" : 1.0
                  }
                }""";
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
        Assertions.assertThat(boolQueryBuilder.must()).isNotEmpty();
        Assertions.assertThat(boolQueryBuilder).hasToString(getTimestampRangeJson());
    }

    private String getTimestampRangeJson() {
        return """
                {
                  "bool" : {
                    "must" : [
                      {
                        "range" : {
                          "timestamp" : {
                            "from" : "2024-03-01T10:52:16",
                            "to" : "2024-03-01T12:52:16",
                            "include_lower" : true,
                            "include_upper" : true,
                            "boost" : 1.0
                          }
                        }
                      }
                    ],
                    "adjust_pure_negative" : true,
                    "boost" : 1.0
                  }
                }""";
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
