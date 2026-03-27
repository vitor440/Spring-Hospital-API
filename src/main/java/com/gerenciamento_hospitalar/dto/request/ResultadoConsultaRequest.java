package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.NotNull;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ResultadoConsultaRequest(@NotNull
                                       Long consultaId,
                                       List<String> sintomas,
                                       String diagnostico,
                                       String notas,
                                       String tratamento,
                                       LocalDate dataRetorno,
                                       PrescricaoRequest prescricao) {
}
