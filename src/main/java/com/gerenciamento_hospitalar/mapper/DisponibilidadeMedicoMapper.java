package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.DisponibilidadeMedicoRequest;
import com.gerenciamento_hospitalar.dto.response.DisponibilidadeMedicoResponse;
import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DisponibilidadeMedicoMapper {

    DisponibilidadeMedico toEntity(DisponibilidadeMedicoRequest request);

    @Mapping(target = "medicoId", expression = "java( disponibilidadeMedico.getMedico().getId() )")
    DisponibilidadeMedicoResponse toDTO(DisponibilidadeMedico disponibilidadeMedico);
}
