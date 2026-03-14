package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.mapper.DepartamentoMapper;
import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.validator.DepartamentoValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.DirectAccessRegion;
import org.springframework.data.domain.*;
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
        Departamento departamento = obterDepartamentoPeloIdOuLancarExcecao(id);

        departamento.setNome(request.nome());
        departamento.setLocalizacao(request.localizacao());

        validator.validar(departamento);
        return mapper.toDTO(departamentoRepository.save(departamento));
    }

    public DepartamentoResponse obterDepartamentoPeloId(Long id) {
        Departamento departamento = obterDepartamentoPeloIdOuLancarExcecao(id);

        return mapper.toDTO(departamento);
    }

    public void deletarDepartamentoPeloId(Long id) {
        Departamento departamento = obterDepartamentoPeloIdOuLancarExcecao(id);

        validator.validarDelecao(departamento);
        departamentoRepository.delete(departamento);
    }

    public Page<DepartamentoResponse> listarDepartamentos(String nome, String localizacao, int pagina, int tamanho, String direction) {

        Departamento departamento = new Departamento();
        departamento.setNome(nome);
        departamento.setLocalizacao(localizacao);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Departamento> example = Example.of(departamento, matcher);

        Sort.Direction sort = direction.equalsIgnoreCase("ASC")? Sort.Direction.ASC: Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort);

        return departamentoRepository.findAll(example, pageable).map(mapper::toDTO);
    }

    private Departamento obterDepartamentoPeloIdOuLancarExcecao(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() ->  new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));
    }
}
