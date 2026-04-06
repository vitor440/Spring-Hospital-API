package com.gerenciamento_hospitalar.dto.response;

import com.gerenciamento_hospitalar.model.Especialidade;

public record MedicoResponse(Long id,
                             String crm,
                             String nome,
                             String email,
                             String telefone,
                             Especialidade especialidade,
                             Long departamentoId) {
}
