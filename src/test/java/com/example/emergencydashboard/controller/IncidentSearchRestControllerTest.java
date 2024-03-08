package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import com.example.emergencydashboard.service.IncidentSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentSearchRestController.class)
class IncidentSearchRestControllerTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentSearchService service;

    @Autowired
    private ObjectMapper objectMapper;

    private IncidentEntityDto incidentEntityDto;

    @BeforeEach
    void setUp() {
        incidentEntityDto = new IncidentEntityDto("1", IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.MEDIUM);
    }

    @Test
    void searchIncidentsByType() throws Exception {
        IncidentType incidentType = IncidentType.FIRE;
        given(service.searchIncidentsByType(incidentType)).willReturn(Collections.singletonList(incidentEntityDto));

        mockMvc.perform(get("/api/v1/incidents/search/{type}", incidentType))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(incidentEntityDto))));
    }

    @Test
    void searchIncidents() throws Exception {
        IncidentSearchQueryDto queryDto = IncidentSearchQueryDto.builder()
                .incidentType(IncidentType.FIRE)
                .latitude(40.712776)
                .longitude(-74.005974)
                .timestamp(NOW)
                .build();

        given(service.searchIncidents(queryDto)).willReturn(Collections.singletonList(incidentEntityDto));

        mockMvc.perform(get("/api/v1/incidents/search")
                        .param("incidentType", queryDto.getIncidentType().getType())
                        .param("latitude", queryDto.getLatitude().toString())
                        .param("longitude", queryDto.getLongitude().toString())
                        .param("timestamp", queryDto.getTimestamp().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(incidentEntityDto))));
    }

}
