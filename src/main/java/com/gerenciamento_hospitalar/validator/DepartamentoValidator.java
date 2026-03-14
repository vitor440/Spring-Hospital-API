package com.gerenciamento_hospitalar.validator;

import com.gerenciamento_hospitalar.exception.DelecaoNaoPermitidaException;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DepartamentoValidator {

    private final DepartamentoRepository departamentoRepository;
    private final MedicoRepository medicoRepository;

    public void validar(Departamento departamento) {
        if(existeDuplicidade(departamento)) {
            throw new RegistroDuplicadoException("Não é permitido o cadastro de departamentos com o mesmo nome!");
        }
    }

    private boolean existeDuplicidade(Departamento departamento) {
        Optional<Departamento> departamentoEncontrado = departamentoRepository.findByNome(departamento.getNome());

        if(departamento.getId() == null) {
            return departamentoEncontrado.isPresent();
        }

        return departamentoEncontrado.map(Departamento::getId)
                .stream()
                .anyMatch(id -> !id.equals(departamento.getId()));
    }


    public void validarDelecao(Departamento departamento) {
        if(medicoRepository.existsByDepartamento(departamento)) {
            throw new DelecaoNaoPermitidaException("Não é permitido deletar departamentos com médicos associados!");
        }
    }
}
