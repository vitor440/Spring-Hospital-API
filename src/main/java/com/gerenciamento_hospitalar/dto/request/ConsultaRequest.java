package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaRequest(@NotNull(message = "campo obrigatório!")
                              Long medicoId,
                              @NotNull(message = "campo obrigatório!")
                              Long pacienteId,
                              String proposito,
                              @NotNull(message = "campo obrigatório!")
                              @Future(message = "A data da consulta não pode ser no passado!")
                              LocalDate data,
                              @NotNull(message = "campo obrigatório!")
                              LocalTime hora) {
}
