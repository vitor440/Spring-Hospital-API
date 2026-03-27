package com.gerenciamento_hospitalar.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PrescricaoResponse(Long id,
                                 Long medicoId,
                                 Long pacienteId,
                                 Long consultaId,
                                 LocalDate dataPrescricao,
                                 String comentarios,
                                 List<PrescricaoMedicamentoResponse> medicamentos) {
}
