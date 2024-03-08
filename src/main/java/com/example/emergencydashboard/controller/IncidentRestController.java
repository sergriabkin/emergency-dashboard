package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
@Validated
public class IncidentRestController {

    private final IncidentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidentEntityDto createIncident(@Valid @RequestBody IncidentEntityDto incidentEntity) {
        return service.saveIncident(incidentEntity);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<IncidentEntityDto> getAllIncidents() {
        return service.findAllIncidents();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public IncidentEntityDto updateIncident(@PathVariable String id, @Valid @RequestBody IncidentEntityDto incidentDto) {
        return service.updateIncident(id, incidentDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIncident(@PathVariable String id) {
        service.deleteIncident(id);
    }

}
