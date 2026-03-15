package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.PacienteMapper;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.repository.specs.PacienteSpecs;
import com.gerenciamento_hospitalar.validator.PacienteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteValidator validator;
    private final PacienteMapper mapper;

    public PacienteResponse addPaciente(PacienteRequest request) {
        Paciente paciente = mapper.toEntity(request);

        validator.validar(paciente);
        return mapper.toDTO(pacienteRepository.save(paciente));
    }

    public PacienteResponse atualizarPaciente(Long id, PacienteRequest request) {
        Paciente paciente = obterPacientePeloIdOuLancarExcecao(id);

        paciente.setCpf(request.cpf());
        paciente.setNome(request.nome());
        paciente.setGenero(request.genero());
        paciente.setEndereco(request.endereco());
        paciente.setEmail(request.email());
        paciente.setTelefone(request.telefone());
        paciente.setTipoSanguineo(request.tipoSanguineo());
        paciente.setDataNascimento(request.dataNascimento());

        validator.validar(paciente);
        return mapper.toDTO(pacienteRepository.save(paciente));
    }

    public PacienteResponse obterPacientePeloId(Long id) {
        return mapper.toDTO(obterPacientePeloIdOuLancarExcecao(id));
    }

    public void deletarPaciente(Long id) {
        Paciente paciente = obterPacientePeloIdOuLancarExcecao(id);
        validator.validarDelecao(paciente);
        pacienteRepository.delete(paciente);
    }

    public Page<PacienteResponse> listarPacientes(String nome, String genero, String tipoSanguineo, int pagina,
                                                  int tamanho, String direction) {

        Specification<Paciente> specs = (root, query, cb) -> cb.conjunction();

        if(nome != null) {
            specs = specs.and(PacienteSpecs.likeNome(nome));
        }

        if(genero != null) {
            specs = specs.and(PacienteSpecs.likeGenero(genero));
        }

        if(tipoSanguineo != null) {
            specs = specs.and(PacienteSpecs.likeTipoSanguineo(tipoSanguineo));
        }

        Sort.Direction sort = direction.equalsIgnoreCase("ASC")? Sort.Direction.ASC: Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "nome");

        return pacienteRepository.findAll(specs, pageable).map(mapper::toDTO);
    }

    private Paciente obterPacientePeloIdOuLancarExcecao(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe paciente com esse ID!"));
    }
}
