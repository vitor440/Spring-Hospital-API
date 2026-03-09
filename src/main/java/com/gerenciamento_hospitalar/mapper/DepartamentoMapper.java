package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.model.Departamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    Departamento toEntity(DepartamentoRequest request);

    DepartamentoResponse toDTO(Departamento departamento);
}
