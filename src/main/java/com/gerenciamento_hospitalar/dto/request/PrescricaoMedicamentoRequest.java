package com.gerenciamento_hospitalar.dto.request;

public record PrescricaoMedicamentoRequest(Long prescricaoId,
                                           Long medicamentoId,
                                           String dosagem,
                                           String frequencia,
                                           String duracao) {
}
