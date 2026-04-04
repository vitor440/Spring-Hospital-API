package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.TurnoAtendimentoRequest;
import com.gerenciamento_hospitalar.dto.response.TurnoAtendimentoResponse;
import com.gerenciamento_hospitalar.model.TurnoAtendimento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TurnoAtendimentoMapper {

    TurnoAtendimento toEntity(TurnoAtendimentoRequest request);

    @Mapping(target = "medicoId", expression = "java( turnoAtendimento.getMedico().getId() )")
    TurnoAtendimentoResponse toDTO(TurnoAtendimento turnoAtendimento);
}
