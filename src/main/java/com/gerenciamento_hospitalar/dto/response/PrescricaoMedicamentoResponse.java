package com.gerenciamento_hospitalar.dto.response;

public record PrescricaoMedicamentoResponse(Long id,
                                            Long prescricaoId,
                                            String nome,
                                            String tipo,
                                            String descricao,
                                            String dosagem,
                                            String frequencia,
                                            String duracao) {
}
