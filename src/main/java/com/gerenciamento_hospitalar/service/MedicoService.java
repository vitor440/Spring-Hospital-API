package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.exception.AcessoNegadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.file.imports.contract.MedicoImporter;
import com.gerenciamento_hospitalar.file.imports.factory.MedicoImporterFactory;
import com.gerenciamento_hospitalar.mapper.MedicoMapper;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Usuario;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.repository.UsuarioRepository;
import com.gerenciamento_hospitalar.validator.MedicoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoValidator validator;
    private final MedicoMapper mapper;
    private final DepartamentoRepository departamentoRepository;
    private final MedicoImporterFactory factory;
    private final SecurityService securityService;
    private final UsuarioRepository usuarioRepository;


    @Transactional
    public MedicoResponse addMedico(MedicoRequest request) {
        Medico medico = mapper.toEntity(request);

        Departamento departamento = obterDepartamentoPeloIdOuLancarExcecao(request.departamentoId());
        Usuario usuario = getObterUsuarioPeloIdOuLancarExcecao(request.userId());

        medico.setDepartamento(departamento);
        medico.setUsuario(usuario);

        validator.validar(medico);
        return mapper.toDTO(medicoRepository.save(medico));
    }

    public MedicoResponse atualizarMedico(Long id, MedicoRequest request) {
        Medico medico = obterMedicoPeloIdOuLancarExcecao(id);

        medico.setCrm(request.crm());
        medico.setNome(request.nome());
        medico.setEmail(request.email());
        medico.setTelefone(request.telefone());
        medico.setEspecialidade(request.especialidade());

        Departamento departamento = obterDepartamentoPeloIdOuLancarExcecao(request.departamentoId());
        Usuario usuario = getObterUsuarioPeloIdOuLancarExcecao(request.userId());

        medico.setDepartamento(departamento);
        medico.setUsuario(usuario);

        validator.validar(medico);
        return mapper.toDTO(medicoRepository.save(medico));
    }

    public MedicoResponse obterMedicoPeloId(Long id) {
        Medico medico = obterMedicoPeloIdOuLancarExcecao(id);

        return mapper.toDTO(medico);
    }

    public MedicoResponse obterMedicoLogado() {
        Usuario usuarioLogado = securityService.getUsuarioLogado();
        Medico medico = obterMedicoLogado(usuarioLogado);

        return mapper.toDTO(medico);
    }

    public void deletarMedicoPeloId(Long id) {

       Medico medico = obterMedicoPeloIdOuLancarExcecao(id);
       validator.validarDelecao(medico);
       medicoRepository.delete(medico);
    }

    public Page<MedicoResponse> ListarMedicos(String nome, Especialidade especialidade, int pagina, int tamanho, String direction) {
        Medico medico = new Medico();

        medico.setNome(nome);
        medico.setEspecialidade(especialidade);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Medico> example = Example.of(medico, matcher);

        Sort.Direction sort = direction.equalsIgnoreCase("ASC")? Sort.Direction.ASC: Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "nome");

        return medicoRepository.findAll(example, pageable).map(mapper::toDTO);
    }


    public List<MedicoResponse> importar(MultipartFile file) {
        String filename = file.getOriginalFilename();

        if(filename == null || filename.isBlank()) {
            throw new RuntimeException("Nome do arquivo .csv inválido.");
        }

        MedicoImporter importer = factory.getImporter(filename);

        try {
            List<Medico> medicos = importer.importFile(file.getInputStream());

            List<MedicoResponse> medicoResponses = medicoRepository.saveAll(medicos).stream()
                    .map(mapper::toDTO)
                    .toList();

            return medicoResponses;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao importar arquivo .csv");
        }
    }




    private Medico obterMedicoPeloIdOuLancarExcecao(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));
    }

    private Departamento obterDepartamentoPeloIdOuLancarExcecao(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));
    }

    private Usuario getObterUsuarioPeloIdOuLancarExcecao(Long userId) {
        return usuarioRepository.findById(userId)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe usuario com esse ID!"));
    }

    private Medico obterMedicoLogado(Usuario usuario) {
        Optional<Medico> medicoOpt = medicoRepository.findByUsuario(usuario);

        if(medicoOpt.isEmpty()) {
            throw new AcessoNegadoException("acesso negado.");
        }

        return medicoOpt.get();
    }
}
