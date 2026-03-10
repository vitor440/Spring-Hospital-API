package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.mapper.DepartamentoMapper;
import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.validator.DepartamentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoValidator validator;
    private final DepartamentoMapper mapper;

    public DepartamentoResponse addDepartamento(DepartamentoRequest request) {
        Departamento departamento = mapper.toEntity(request);

        validator.validar(departamento);
        return mapper.toDTO(departamentoRepository.save(departamento));
    }

    public DepartamentoResponse atualizarDepartamento(Long id, DepartamentoRequest request) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

        departamento.setNome(request.nome());
        departamento.setLocalizacao(request.localizacao());

        validator.validar(departamento);
        return mapper.toDTO(departamentoRepository.save(departamento));
    }

    public DepartamentoResponse obterDepartamentoPeloId(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

        return mapper.toDTO(departamento);
    }

    public void deletarDepartamentoPeloId(Long id) {
        Departamento departamento = departamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

        validator.validarDelecao(departamento);
        departamentoRepository.delete(departamento);
    }
}
