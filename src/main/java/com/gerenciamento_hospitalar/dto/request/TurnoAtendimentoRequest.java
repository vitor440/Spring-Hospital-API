package com.gerenciamento_hospitalar.dto.request;

import com.gerenciamento_hospitalar.model.DiaSemana;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TurnoAtendimentoRequest(@NotNull(message = "campo obrigatório!")
                                      DiaSemana diaSemana,
                                      @NotNull(message = "campo obrigatório!")
                                           LocalTime horaInicio,
                                      @NotNull(message = "campo obrigatório!")
                                           LocalTime horaFim) {
}
