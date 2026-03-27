package com.gerenciamento_hospitalar.dto.response;

public record PrescricaoMedicamentoResponse(Long id,
                                            Long prescricaoId,
                                            Long medicamentoId,
                                            String dosagem,
                                            String frequencia,
                                            String duracao) {
}
