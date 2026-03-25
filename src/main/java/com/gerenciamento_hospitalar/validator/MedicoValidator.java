package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MedicoValidator {

    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    public void validar(Medico medico) {
        if(crmDuplicado(medico)) {
            throw new RegistroDuplicadoException("Não é permitido o cadastro de médicos com o mesmo crm!");
        }
    }

    private boolean crmDuplicado(Medico medico) {
        Optional<Medico> medicoEncontrado = medicoRepository.findByCrm(medico.getCrm());

        if(medico.getId() == null) {
            return medicoEncontrado.isPresent();
        }

        return medicoEncontrado.map(Medico::getId)
                .stream()
                .anyMatch(id -> !id.equals(medico.getId()));
    }

    public void validarDelecao(Medico medico) {
        if (consultaRepository.existsByMedico(medico)) {
            throw new DelecaoNaoPermitidaException("Não é permitido a exclusão de médicos com consultas cadastradas!");
        }
    }
}
