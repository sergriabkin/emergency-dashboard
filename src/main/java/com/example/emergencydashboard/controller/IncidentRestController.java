package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
public class IncidentRestController {

    private final IncidentService service;

    @PostMapping
    public IncidentEntityDto createIncident(@RequestBody IncidentEntityDto incidentEntity) {
        return service.saveIncident(incidentEntity);
    }

    @GetMapping
    public List<IncidentEntityDto> getAllIncidents() {
        return service.findAllIncidents();
    }

    @GetMapping("/search")
    public List<IncidentEntityDto> searchIncidents(@RequestParam String query) {
        return service.searchIncidents(query);
    }
}
