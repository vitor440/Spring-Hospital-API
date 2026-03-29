package com.gerenciamento_hospitalar.dto.request;

import com.gerenciamento_hospitalar.model.Consulta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;
import java.util.List;

public record PrescricaoRequest(@NotNull
                                LocalDate dataPrescricao,
                                @NotBlank
                                String comentarios,
                                List<PrescricaoMedicamentoRequest> medicamentos) {
}
