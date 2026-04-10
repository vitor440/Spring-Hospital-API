package com.gerenciamento_hospitalar.dto.request;

public record MedicamentoRequest(String nome,
                                 String tipo,
                                 String descricao,
                                 String dosagem,
                                 String frequencia,
                                 String duracao) {
}
