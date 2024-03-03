package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
@Validated
public class IncidentRestController {

    private final IncidentService service;

    @PostMapping
    public IncidentEntityDto createIncident(@Valid @RequestBody IncidentEntityDto incidentEntity) {
        return service.saveIncident(incidentEntity);
    }

    @GetMapping
    public List<IncidentEntityDto> getAllIncidents() {
        return service.findAllIncidents();
    }

    @GetMapping("/search/{type}")
    public List<IncidentEntityDto> searchIncidentsByType(@PathVariable String type) {
        return service.searchIncidentsByType(type);
    }

    @GetMapping("/search")
    public List<IncidentEntityDto> searchIncidents(
            @RequestParam(required = false) String incidentType,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        var queryDto = IncidentSearchQueryDto.builder()
                .incidentType(incidentType)
                .latitude(latitude)
                .longitude(longitude)
                .timestamp(timestamp)
                .build();

        return service.searchIncidents(queryDto);
    }
}
