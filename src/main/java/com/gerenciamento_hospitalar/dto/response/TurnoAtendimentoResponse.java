package com.gerenciamento_hospitalar.dto.response;

import com.gerenciamento_hospitalar.model.DiaSemana;

import java.time.LocalTime;

public record TurnoAtendimentoResponse(Long id,
                                       Long medicoId,
                                       DiaSemana diaSemana,
                                       LocalTime horaInicio,
                                       LocalTime horaFim) {
}
