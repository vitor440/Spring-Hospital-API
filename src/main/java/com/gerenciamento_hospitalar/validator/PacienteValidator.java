package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PacienteValidator {

    private final PacienteRepository pacienteRepository;

    public void validar(Paciente paciente) {
        if(cpfDuplicado(paciente)) {
            throw new RegistroDuplicadoException("Não é permitido o cadastro de pacientes com o mesmo cpf!");
        }
    }

    private boolean cpfDuplicado(Paciente paciente) {
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findByCpf(paciente.getCpf());

        if(paciente.getId() == null) {
            return pacienteEncontrado.isPresent();
        }

        return pacienteEncontrado.map(Paciente::getId)
                .stream()
                .anyMatch(id -> !id.equals(paciente.getId()));
    }
}
