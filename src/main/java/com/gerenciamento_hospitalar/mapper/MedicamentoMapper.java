package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicamentoResponse;
import com.gerenciamento_hospitalar.model.Medicamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicamentoMapper {

    Medicamento toEntity(MedicamentoRequest request);

    @Mapping(target = "prescricaoId", expression = "java( medicamento.getPrescricao().getId() )")
    MedicamentoResponse toDTO(Medicamento medicamento);

    List<Medicamento> toEntityList(List<MedicamentoRequest> requests);
}
