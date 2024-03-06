package com.example.emergencydashboard.controller;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.dto.IncidentSearchQueryDto;
import com.example.emergencydashboard.model.IncidentType;
import com.example.emergencydashboard.model.SeverityLevel;
import com.example.emergencydashboard.service.IncidentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.example.emergencydashboard.dto.IncidentEntityDto.LATITUDE_RANGE_MESSAGE;
import static com.example.emergencydashboard.dto.IncidentEntityDto.LONGITUDE_RANGE_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IncidentRestController.class)
class IncidentRestControllerTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService service;

    @Autowired
    private ObjectMapper objectMapper;

    private IncidentEntityDto incidentEntityDto;

    @BeforeEach
    void setUp() {
        incidentEntityDto = new IncidentEntityDto("1", IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.MEDIUM);
    }

    @Test
    void createIncident() throws Exception {
        given(service.saveIncident(incidentEntityDto)).willReturn(incidentEntityDto);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incidentEntityDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(incidentEntityDto)));
    }

    @Test
    void createIncident_WithInvalidLatitude_ReturnsBadRequest() throws Exception {
        IncidentEntityDto invalidIncident = new IncidentEntityDto("1", IncidentType.FIRE, 90.1, -74.005974, NOW, SeverityLevel.MEDIUM);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidIncident)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.latitude").value(LATITUDE_RANGE_MESSAGE));
    }

    @Test
    void createIncident_WithInvalidLongitude_ReturnsBadRequest() throws Exception {
        IncidentEntityDto invalidIncident = new IncidentEntityDto("1", IncidentType.FIRE, -90.0, -180.1, NOW, SeverityLevel.MEDIUM);

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidIncident)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message.longitude").value(LONGITUDE_RANGE_MESSAGE));
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
        IncidentType incidentType = IncidentType.FIRE;
        given(service.searchIncidentsByType(incidentType)).willReturn(Collections.singletonList(incidentEntityDto));

        mockMvc.perform(get("/incidents/search/{type}", incidentType))
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

        mockMvc.perform(get("/incidents/search")
                        .param("incidentType", queryDto.getIncidentType().getType())
                        .param("latitude", queryDto.getLatitude().toString())
                        .param("longitude", queryDto.getLongitude().toString())
                        .param("timestamp", queryDto.getTimestamp().toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(incidentEntityDto))));
    }

    @Test
    void updateIncident() throws Exception {
        IncidentEntityDto updatedDto = new IncidentEntityDto("1", IncidentType.FIRE, 41.712776, -74.005974, NOW, SeverityLevel.HIGH);

        given(service.updateIncident(anyString(), any(IncidentEntityDto.class))).willReturn(updatedDto);

        mockMvc.perform(put("/incidents/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedDto)));
    }

    @Test
    void deleteIncident() throws Exception {
        doNothing().when(service).deleteIncident(anyString());

        mockMvc.perform(delete("/incidents/{id}", "1"))
                .andExpect(status().isNoContent());

        verify(service).deleteIncident("1");
    }

    @Test
    void whenUnexpectedException_thenRespondInternalServerError() throws Exception {
        IncidentEntityDto incidentEntity = new IncidentEntityDto("1", IncidentType.FIRE, 40.712776, -74.005974, NOW, SeverityLevel.MEDIUM);

        given(service.saveIncident(any(IncidentEntityDto.class))).willThrow(new RuntimeException("Unexpected error message"));

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incidentEntity)))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RuntimeException))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred: Unexpected error message"));
    }

    @Test
    void whenEntityNotFound_thenRespondNotFound() throws Exception {
        given(service.updateIncident(anyString(), any(IncidentEntityDto.class)))
                .willThrow(new EntityNotFoundException("Incident not found"));

        mockMvc.perform(put("/incidents/{id}", "non-existing-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incidentEntityDto)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(jsonPath("$.error").value("Entity Not Found"))
                .andExpect(jsonPath("$.message").value("Incident not found"));
    }

    @Test
    void createIncident_WithInvalidIncidentType_ReturnsBadRequest() throws Exception {
        String invalidIncidentTypePayload = """
        {
            "incidentType": "invalid_type",
            "latitude": 40.712776,
            "longitude": -74.005974,
            "timestamp": "%s",
            "severityLevel": "MEDIUM"
        }
        """.formatted(NOW.toString());

        mockMvc.perform(post("/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidIncidentTypePayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Error"))
                .andExpect(jsonPath("$.message").value("Invalid incident type: invalid_type"));
    }

}
