package com.example.emergencydashboard.mapper;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Mapper
public interface IncidentMapper {

    IncidentMapper INSTANCE = Mappers.getMapper(IncidentMapper.class);

    @Mapping(target = "incidentType", source = "entity.incidentType", defaultExpression = "java(com.example.emergencydashboard.model.IncidentType.NONE)")
    @Mapping(target = "severityLevel", source = "entity.severityLevel", defaultExpression = "java(com.example.emergencydashboard.model.SeverityLevel.NONE)")
    IncidentEntityDto entityToDto(IncidentEntity entity);

    @Mapping(target = "location", source = "entity", qualifiedByName = "latLonEntityToGeoPoint")
    @Mapping(target = "incidentType", source = "entity.incidentType", defaultExpression = "java(com.example.emergencydashboard.model.IncidentType.NONE)")
    @Mapping(target = "severityLevel", source = "entity.severityLevel", defaultExpression = "java(com.example.emergencydashboard.model.SeverityLevel.NONE)")
    IncidentDocument entityToDocument(IncidentEntity entity);

    @Mapping(target = "incidentType", source = "dto.incidentType", defaultExpression = "java(com.example.emergencydashboard.model.IncidentType.NONE)")
    @Mapping(target = "severityLevel", source = "dto.severityLevel", defaultExpression = "java(com.example.emergencydashboard.model.SeverityLevel.NONE)")
    IncidentEntity dtoToEntity(IncidentEntityDto dto);

    @Mapping(target = "location", source = "dto", qualifiedByName = "latLonDtoToGeoPoint")
    @Mapping(target = "incidentType", source = "dto.incidentType", defaultExpression = "java(com.example.emergencydashboard.model.IncidentType.NONE)")
    @Mapping(target = "severityLevel", source = "dto.severityLevel", defaultExpression = "java(com.example.emergencydashboard.model.SeverityLevel.NONE)")
    IncidentDocument dtoToDocument(IncidentEntityDto dto);

    @Mapping(target = "latitude", source = "location.lat")
    @Mapping(target = "longitude", source = "location.lon")
    @Mapping(target = "incidentType", source = "document.incidentType", defaultExpression = "java(com.example.emergencydashboard.model.IncidentType.NONE)")
    @Mapping(target = "severityLevel", source = "document.severityLevel", defaultExpression = "java(com.example.emergencydashboard.model.SeverityLevel.NONE)")
    IncidentEntity documentToEntity(IncidentDocument document);

    @Mapping(target = "latitude", source = "location.lat")
    @Mapping(target = "longitude", source = "location.lon")
    @Mapping(target = "incidentType", source = "document.incidentType", defaultExpression = "java(com.example.emergencydashboard.model.IncidentType.NONE)")
    @Mapping(target = "severityLevel", source = "document.severityLevel", defaultExpression = "java(com.example.emergencydashboard.model.SeverityLevel.NONE)")
    IncidentEntityDto documentToDto(IncidentDocument document);

    @Named("latLonDtoToGeoPoint")
    static GeoPoint latLonDtoToGeoPoint(IncidentEntityDto dto) {
        return new GeoPoint(dto.getLatitude(), dto.getLongitude());
    }

    @Named("latLonEntityToGeoPoint")
    static GeoPoint latLonEntityToGeoPoint(IncidentEntity entity) {
        return new GeoPoint(entity.getLatitude(), entity.getLongitude());
    }
}
