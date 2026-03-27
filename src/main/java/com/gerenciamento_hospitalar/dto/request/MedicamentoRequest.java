package com.gerenciamento_hospitalar.dto.request;

public record MedicamentoRequest(String nome,
                                 String marca,
                                 String tipo,
                                 String descricao,
                                 int estoque) {
}
