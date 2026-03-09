package com.gerenciamento_hospitalar.dto.response;

public record PacienteResponse(Long id,
                               String cpf,
                               String nome,
                               String genero,
                               String endereco,
                               String email,
                               String telefone,
                               String tipoSanguineo,
                               String dataNascimento) {
}
