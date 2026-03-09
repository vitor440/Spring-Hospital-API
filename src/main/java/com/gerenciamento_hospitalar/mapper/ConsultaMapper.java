package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.model.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {

    Consulta toEntity(ConsultaRequest request);

    @Mapping(target = "medicoId", expression = "java( consulta.getMedico().getId() )")
    @Mapping(target = "pacienteId", expression = "java( consulta.getPaciente().getId() )")
    ConsultaResponse toDTO(Consulta consulta);
}
