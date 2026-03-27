package com.gerenciamento_hospitalar.mapper;

import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.model.ResultadoConsulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PrescricaoMapper.class)
public interface ResultadoConsultaMapper {

    ResultadoConsulta toEntity(ResultadoConsultaRequest request);

    @Mapping(target = "consultaId", expression = "java( resultadoConsulta.getConsulta().getId() )")
    ResultadoConsultaResponse toDTO(ResultadoConsulta resultadoConsulta);
}
