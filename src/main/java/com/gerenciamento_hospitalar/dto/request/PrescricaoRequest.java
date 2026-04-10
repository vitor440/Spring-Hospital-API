package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PrescricaoRequest(@NotNull
                                LocalDate dataPrescricao,
                                @NotBlank
                                String comentarios,
                                List<MedicamentoRequest> medicamentos) {
}
