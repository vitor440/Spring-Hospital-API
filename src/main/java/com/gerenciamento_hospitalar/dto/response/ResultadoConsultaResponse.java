package com.gerenciamento_hospitalar.dto.response;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ResultadoConsultaResponse(Long id,
                                        Long medicoId,
                                        Long pacienteId,
                                        Long consultaId,
                                        List<String> sintomas,
                                        String diagnostico,
                                        String notas,
                                        String tratamento,
                                        LocalDate dataRetorno,
                                        LocalDateTime dataCriacao,
                                        PrescricaoResponse prescricao) {
}
