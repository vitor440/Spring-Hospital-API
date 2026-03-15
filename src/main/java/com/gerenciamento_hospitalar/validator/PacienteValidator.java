package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PacienteValidator {

    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

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

    public void validarDelecao(Paciente paciente) {
        if(consultaRepository.existsByPaciente(paciente)) {
            throw new DelecaoNaoPermitidaException("Não é permitido a exclusão de pacientes com consultas cadastradas!");
        }
    }
}
