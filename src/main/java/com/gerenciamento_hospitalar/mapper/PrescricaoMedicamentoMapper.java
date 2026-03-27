package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoMedicamentoResponse;
import com.gerenciamento_hospitalar.model.PrescricaoMedicamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescricaoMedicamentoMapper {

    PrescricaoMedicamento toEntity(PrescricaoMedicamentoRequest request);

    @Mapping(target = "prescricaoId", expression = "java( prescricaoMedicamento.getPrescricao().getId() )")
    @Mapping(target = "medicamentoId", expression = "java( prescricaoMedicamento.getMedicamento().getId() )")
    PrescricaoMedicamentoResponse toDTO(PrescricaoMedicamento prescricaoMedicamento);

    List<PrescricaoMedicamento> toEntityList(List<PrescricaoMedicamentoRequest> requests);
}
