package com.gerenciamento_hospitalar.dto.response;

import java.time.LocalTime;

public record DisponibilidadeMedicoResponse(Long id,
                                            Integer diaSemana,
                                            LocalTime horaInicio,
                                            LocalTime horaFim,
                                            MedicoResponse medico) {
}
