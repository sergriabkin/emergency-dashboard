package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSocketController {
    private static final String INCIDENT_SAVED_TEMPLATE = "incident {} is successfully saved and gonna broadcast to all subscribers";

    private final IncidentService service;

    @MessageMapping("/incident")
    @SendTo("/topic/incidents")
    public IncidentEntityDto notifyIncident(IncidentEntityDto incident) {
        IncidentEntityDto savedIncident = service.saveIncident(incident);
        log.info(INCIDENT_SAVED_TEMPLATE, savedIncident.getId());
        return savedIncident;
    }
}
