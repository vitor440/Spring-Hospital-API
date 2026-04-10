package com.gerenciamento_hospitalar.dto.response;

import java.time.LocalDate;

public record PacienteResponse(Long id,
                               String cpf,
                               String nome,
                               String genero,
                               String endereco,
                               String email,
                               String telefone,
                               String tipoSanguineo,
                               LocalDate dataNascimento) {
}
