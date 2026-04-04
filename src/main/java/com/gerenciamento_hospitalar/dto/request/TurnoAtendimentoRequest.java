package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TurnoAtendimentoRequest(@NotNull(message = "campo obrigatório!")
                                           @Min(value = 1, message = "dia da semana fora do intervalo (1 a 7)!")
                                           @Max(value = 7, message = "dia da semana fora do intervalo (1 a 7)!")
                                           Integer diaSemana,
                                      @NotNull(message = "campo obrigatório!")
                                           LocalTime horaInicio,
                                      @NotNull(message = "campo obrigatório!")
                                           LocalTime horaFim,
                                      @NotNull(message = "campo obrigatório!")
                                           Long medicoId) {
}
