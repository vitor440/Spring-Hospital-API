package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicamentoResponse;
import com.gerenciamento_hospitalar.model.Medicamento;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicamentoMapper {

    Medicamento toEntity(MedicamentoRequest request);

    MedicamentoResponse toDTO(Medicamento medicamento);

}
