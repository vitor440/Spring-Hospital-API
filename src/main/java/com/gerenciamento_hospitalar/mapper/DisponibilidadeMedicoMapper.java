package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.DisponibilidadeMedicoRequest;
import com.gerenciamento_hospitalar.dto.response.DisponibilidadeMedicoResponse;
import com.gerenciamento_hospitalar.model.DisponibilidadeMedico;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MedicoMapper.class)
public interface DisponibilidadeMedicoMapper {

    DisponibilidadeMedico toEntity(DisponibilidadeMedicoRequest request);


    DisponibilidadeMedicoResponse toDTO(DisponibilidadeMedico disponibilidadeMedico);
}
