package com.gerenciamento_hospitalar.dto.security;

import jakarta.validation.constraints.NotBlank;

public record CadastroUsuarioDTO(Long id,
                                 @NotBlank(message = "campo obrigatório")
                                 String username,
                                 @NotBlank(message = "campo obrigatório")
                                 String senha,
                                 String nomeCompleto) {
}
