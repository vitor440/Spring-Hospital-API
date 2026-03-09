package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaRequest(@NotNull
                              Long medicoId,
                              @NotNull
                              Long pacienteId,
                              String proposito,
                              @NotNull
                              @Future
                              LocalDate data,
                              @NotNull
                              LocalTime hora) {
}
