package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.example.emergencydashboard.dto.IncidentEntityDto.LATITUDE_RANGE_MESSAGE;
import static com.example.emergencydashboard.dto.IncidentEntityDto.LONGITUDE_RANGE_MESSAGE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidentRestController.class)
class IncidentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private IncidentEntityDto incidentEntityDto;

    @BeforeEach
    void setUp() {
        incidentEntityDto = new IncidentEntityDto("1", "fire", 40.712776, -74.005974, LocalDateTime.now(), "medium");
    }

    @Test
    void createIncident() throws Exception {
        given(service.saveIncident(incidentEntityDto)).willReturn(incidentEntityDto);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incidentEntityDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(incidentEntityDto)));
    }

    @Test
    void createIncident_WithInvalidLatitude_ReturnsBadRequest() throws Exception {
        IncidentEntityDto invalidIncident = new IncidentEntityDto("1", "fire", 90.1, -74.005974, LocalDateTime.now(), "medium");

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidIncident)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.latitude").value(LATITUDE_RANGE_MESSAGE));
    }

    @Test
    void createIncident_WithInvalidLongitude_ReturnsBadRequest() throws Exception {
        IncidentEntityDto invalidIncident = new IncidentEntityDto("1", "fire", -90.0, -180.1, LocalDateTime.now(), "medium");

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidIncident)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.longitude").value(LONGITUDE_RANGE_MESSAGE));
    }


    @Test
    void getAllIncidents() throws Exception {
        given(service.findAllIncidents()).willReturn(Collections.singletonList(incidentEntityDto));

        mockMvc.perform(get("/incidents"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(incidentEntityDto))));
    }

    @Test
    void searchIncidentsByType() throws Exception {
        String incidentType = "fire";
        given(service.searchIncidentsByType(incidentType)).willReturn(Collections.singletonList(incidentEntityDto));

        mockMvc.perform(get("/incidents/search/{type}", incidentType))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(incidentEntityDto))));
    }

    @Test
    void searchIncidents() throws Exception {
        IncidentSearchQueryDto queryDto = IncidentSearchQueryDto.builder()
                .incidentType("fire")
                .latitude(40.712776)
                .longitude(-74.005974)
                .timestamp(LocalDateTime.now())
                .build();

        given(service.searchIncidents(queryDto)).willReturn(Collections.singletonList(incidentEntityDto));

        mockMvc.perform(get("/incidents/search")
                        .param("incidentType", queryDto.getIncidentType())
                        .param("latitude", queryDto.getLatitude().toString())
                        .param("longitude", queryDto.getLongitude().toString())
                        .param("timestamp", queryDto.getTimestamp().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(incidentEntityDto))));
    }

}
