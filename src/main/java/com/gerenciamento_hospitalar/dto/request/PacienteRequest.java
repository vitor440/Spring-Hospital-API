package com.gerenciamento_hospitalar.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PacienteRequest(@CPF(message = "cpf inválido!")
                              @NotBlank(message = "campo obrigatório!")
                              String cpf,
                              @NotBlank(message = "campo obrigatório!")
                              String nome,
                              @NotBlank(message = "campo obrigatório!")
                              String genero,
                              @NotBlank(message = "campo obrigatório!")
                              String endereco,
                              @NotBlank(message = "campo obrigatório!")
                              String email,
                              @NotBlank(message = "campo obrigatório!")
                              String telefone,
                              @NotBlank(message = "campo obrigatório!")
                              String tipoSanguineo,
                              @NotNull(message = "campo obrigatório!")
                              @Past
                              LocalDate dataNascimento) {
}
