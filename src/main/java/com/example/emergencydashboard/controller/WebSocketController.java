package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.service.IncidentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
@Valid
public class WebSocketController {
    private static final String RECEIVED_INCIDENT_TEMPLATE = "Received incident: {}";
    private static final String INCIDENT_SAVED_TEMPLATE = "Incident {} is successfully saved. Broadcasting it to all subscribers";

    private final IncidentService service;

    @MessageMapping("/incident")
    @SendTo("/topic/incidents")
    public IncidentEntityDto notifyIncident(@Valid IncidentEntityDto incident) {
        log.info(RECEIVED_INCIDENT_TEMPLATE, incident);
        IncidentEntityDto savedIncident = service.saveIncident(incident);
        log.info(INCIDENT_SAVED_TEMPLATE, savedIncident.getId());
        return savedIncident;
    }
}
