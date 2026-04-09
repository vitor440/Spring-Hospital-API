package com.gerenciamento_hospitalar.dto.request;

import com.gerenciamento_hospitalar.model.Especialidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicoRequest(
        @NotBlank(message = "campo obrigatório!")
        String crm,
        @NotBlank(message = "campo obrigatório!")
        String nome,
        @NotBlank(message = "campo obrigatório!")
        String email,
        @NotBlank(message = "campo obrigatório!")
        String telefone,
        @NotNull(message = "campo obrigatório!")
        Especialidade especialidade,
        @NotNull(message = "campo obrigatório!")
        Long departamentoId,
        Long userId) {
}
