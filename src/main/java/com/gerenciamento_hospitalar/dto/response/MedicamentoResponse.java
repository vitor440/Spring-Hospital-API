package com.gerenciamento_hospitalar.dto.response;

public record MedicamentoResponse(Long id,
                                  String nome,
                                  String marca,
                                  String tipo,
                                  String descricao,
                                  int estoque) {
}
