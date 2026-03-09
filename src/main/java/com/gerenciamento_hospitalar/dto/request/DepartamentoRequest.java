package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DepartamentoRequest(@NotBlank(message = "campo obrigatório!")
                                  String nome,
                                  @NotBlank(message = "campo obrigatório!")
                                  String localizacao) {
}
