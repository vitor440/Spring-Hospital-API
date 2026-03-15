package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.mapper.MedicoMapper;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Especialidade;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.validator.MedicoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoValidator validator;
    private final MedicoMapper mapper;
    private final ConsultaMapper consultaMapper;
    private final DepartamentoRepository departamentoRepository;

    public MedicoResponse addMedico(MedicoRequest request) {
        Medico medico = mapper.toEntity(request);

        Departamento departamento = obterDepartamentoPeloIdOuLancarExcecao(request.departamentoId());

        medico.setDepartamento(departamento);

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

        medico.setDepartamento(departamento);

        validator.validar(medico);
        return mapper.toDTO(medicoRepository.save(medico));
    }

    public MedicoResponse obterMedicoPeloId(Long id) {
        return mapper.toDTO(obterMedicoPeloIdOuLancarExcecao(id));
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

    @Transactional
    public List<ConsultaResponse> obterConsultas(Long id) {
        Medico medico = obterMedicoPeloIdOuLancarExcecao(id);

        List<Consulta> consultas = medico.getConsultas();

        return consultas.stream().map(consultaMapper::toDTO).collect(Collectors.toList());
    }

    private Medico obterMedicoPeloIdOuLancarExcecao(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));
    }

    private Departamento obterDepartamentoPeloIdOuLancarExcecao(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));
    }
}
