package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.model.IncidentEntity;
import com.example.emergencydashboard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService service;

    @PostMapping
    public IncidentEntity createIncident(@RequestBody IncidentEntity incidentEntity) {
        return service.saveIncident(incidentEntity);
    }

    @GetMapping
    public List<IncidentEntity> getAllIncidents() {
        return service.findAllIncidents();
    }

    @GetMapping("/search")
    public List<IncidentEntity> searchIncidents(@RequestParam String query) {
        return service.searchIncidents(query);
    }
}
