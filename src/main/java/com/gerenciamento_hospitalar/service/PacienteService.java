package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.contract.PacienteImporter;
import com.gerenciamento_hospitalar.file.imports.factory.PacienteImporterFactory;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.mapper.PacienteMapper;
import com.gerenciamento_hospitalar.model.*;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.repository.RoleRepository;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.repository.specs.PacienteSpecs;
import com.gerenciamento_hospitalar.validator.PacienteValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteValidator validator;
    private final PacienteImporterFactory factory;
    private final PacienteMapper mapper;
    private final ConsultaMapper consultaMapper;
    private final SecurityService securityService;
    private final UsuarioRepository usuarioRepository;

    public PacienteResponse addPaciente(PacienteRequest request) {
        Paciente paciente = mapper.toEntity(request);

        Usuario usuario = getObterUsuarioPeloIdOuLancarExcecao(request.userId());
        paciente.setUsuario(usuario);

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
        Paciente paciente = obterPacientePeloIdOuLancarExcecao(id);
        securityService.validaUsuarioPaciente(paciente);
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

    @Transactional
    public List<ConsultaResponse> historicoConsultas(Long id) {
        Paciente paciente = obterPacientePeloIdOuLancarExcecao(id);
        securityService.validaUsuarioPaciente(paciente);

        List<Consulta> consultas = paciente.getConsultas();

        if(consultas == null || consultas.isEmpty()) return List.of();

        return consultas.stream().map(consultaMapper::toDTO)
                .toList();
    }

    @Transactional
    public List<ConsultaResponse> listarConsultasAgendadas(Long id) {
        Paciente paciente = obterPacientePeloIdOuLancarExcecao(id);
        securityService.validaUsuarioPaciente(paciente);

        List<Consulta> consultas = paciente.getConsultas().stream()
                .filter(consulta -> consulta.getStatus() == StatusConsulta.AGENDADA)
                .toList();

        if(consultas == null || consultas.isEmpty()) return List.of();

        return consultas.stream().map(consultaMapper::toDTO)
                .toList();
    }



    public List<PacienteResponse> importar(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if(filename.isBlank() || filename == null) {
            throw new RuntimeException("Nome do arquivo .csv inválido.");
        }

        PacienteImporter importer = factory.getImporter(filename);

        try {
            List<Paciente> pacientes = importer.importFile(file.getInputStream());

            List<PacienteResponse> pacienteResponses = pacienteRepository.saveAll(pacientes).stream()
                    .map(mapper::toDTO)
                    .toList();

            return pacienteResponses;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao importar arquivo .csv");
        }
    }

    private Paciente obterPacientePeloIdOuLancarExcecao(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe paciente com esse ID!"));
    }

    private Usuario getObterUsuarioPeloIdOuLancarExcecao(Long userId) {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe usuario com esse ID!"));
    }
}
