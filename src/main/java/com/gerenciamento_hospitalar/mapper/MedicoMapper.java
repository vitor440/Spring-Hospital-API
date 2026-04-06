package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.model.Medico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DepartamentoMapper.class)
public interface MedicoMapper {

    Medico toEntity(MedicoRequest request);

    @Mapping(target = "departamentoId", expression = "java( medico.getDepartamento().getId() )")
    MedicoResponse toDTO(Medico medico);
}
