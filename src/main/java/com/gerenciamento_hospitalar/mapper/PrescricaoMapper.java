package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.PrescricaoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import com.gerenciamento_hospitalar.model.Prescricao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MedicamentoMapper.class)
public interface PrescricaoMapper {

    Prescricao toEntity(PrescricaoRequest request);

    @Mapping(target = "consultaId", expression = "java( prescricao.getConsulta().getId() )")
    PrescricaoResponse toDTO(Prescricao prescricao);
}
