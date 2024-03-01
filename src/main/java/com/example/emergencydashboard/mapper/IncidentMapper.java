package com.example.emergencydashboard.mapper;

import com.example.emergencydashboard.dto.IncidentEntityDto;
import com.example.emergencydashboard.model.IncidentDocument;
import com.example.emergencydashboard.model.IncidentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IncidentMapper {

    IncidentMapper INSTANCE = Mappers.getMapper(IncidentMapper.class);

    IncidentEntityDto entityToDto(IncidentEntity entity);
    IncidentDocument entityToDocument(IncidentEntity entity);

    IncidentEntity dtoToEntity(IncidentEntityDto dto);
    IncidentDocument dtoToDocument(IncidentEntityDto dto);

    IncidentEntity documentToEntity(IncidentDocument document);
    IncidentEntityDto documentToDto(IncidentDocument document);

}
