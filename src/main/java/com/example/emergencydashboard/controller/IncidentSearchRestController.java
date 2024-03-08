package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.service.IncidentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.emergencydashboard.dto.IncidentEntityDto.*;

@RestController
@RequestMapping("/api/v1/incidents/search")
@RequiredArgsConstructor
@Validated
public class IncidentSearchRestController {

    private final IncidentSearchService service;

    @GetMapping("/{type}")
    @ResponseStatus(HttpStatus.OK)
    public List<IncidentEntityDto> searchIncidentsByType(@PathVariable String type) {
        return service.searchIncidentsByType(IncidentType.forValue(type));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IncidentEntityDto> searchIncidents(
            @RequestParam(required = false)
            String incidentType,
            @RequestParam(required = false)
            @DecimalMin(value = LATITUDE_MIN, message = LATITUDE_RANGE_MESSAGE)
            @DecimalMax(value = LATITUDE_MAX, message = LATITUDE_RANGE_MESSAGE)
            Double latitude,
            @RequestParam(required = false)
            @DecimalMin(value = LONGITUDE_MIN, message = LONGITUDE_RANGE_MESSAGE)
            @DecimalMax(value = LONGITUDE_MAX, message = LONGITUDE_RANGE_MESSAGE)
            Double longitude,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime timestamp
    ) {

        var queryDto = IncidentSearchQueryDto.builder()
                .incidentType(IncidentType.forValue(incidentType))
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(timestamp)
                .build();

        return service.searchIncidents(queryDto);
    }
}
