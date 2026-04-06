package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.contract.DepartamentoImporter;
import com.gerenciamento_hospitalar.file.imports.contract.PacienteImporter;
import com.gerenciamento_hospitalar.file.imports.factory.DepartamentoImporterFactory;
import com.gerenciamento_hospitalar.mapper.DepartamentoMapper;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.validator.DepartamentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoValidator validator;
    private final DepartamentoMapper mapper;
    private final DepartamentoImporterFactory factory;

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
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "nome");

        return departamentoRepository.findAll(example, pageable).map(mapper::toDTO);
    }


    public List<DepartamentoResponse> importar(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if(filename.isBlank() || filename == null) {
            throw new RuntimeException("Nome do arquivo .csv inválido.");
        }

        DepartamentoImporter importer = factory.getImporter(filename);

        try {
            List<Departamento> departamentos = importer.importFile(file.getInputStream());

            List<DepartamentoResponse> departamentoResponses = departamentoRepository.saveAll(departamentos).stream()
                    .map(mapper::toDTO)
                    .toList();

            return departamentoResponses;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao importar arquivo .csv");
        }
    }

    private Departamento obterDepartamentoPeloIdOuLancarExcecao(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() ->  new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));
    }
}
