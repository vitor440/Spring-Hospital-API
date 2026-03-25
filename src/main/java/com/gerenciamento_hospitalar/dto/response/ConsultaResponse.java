package com.gerenciamento_hospitalar.dto.response;

import com.gerenciamento_hospitalar.model.StatusConsulta;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaResponse(Long id,
                               Long medicoId,
                               Long pacienteId,
                               String proposito,
                               LocalDate data,
                               LocalTime hora,
                               Integer diaSemana,
                               StatusConsulta status) {
}
