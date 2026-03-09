package com.gerenciamento_hospitalar.dto.response;

import java.time.LocalDateTime;

public record DepartamentoResponse(Long id, String nome, String localizacao, LocalDateTime dataCriacao) {
}
