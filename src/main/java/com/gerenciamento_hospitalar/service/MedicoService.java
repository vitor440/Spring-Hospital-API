package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.MedicoMapper;
import com.gerenciamento_hospitalar.model.Departamento;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.repository.DepartamentoRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper mapper;
    private final DepartamentoRepository departamentoRepository;

    public MedicoResponse addMedico(MedicoRequest request) {
        Medico medico = mapper.toEntity(request);

        Departamento departamento = departamentoRepository.findById(request.departamentoId())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

        medico.setDepartamento(departamento);
        return mapper.toDTO(medicoRepository.save(medico));
    }

    public MedicoResponse atualizarMedico(Long id, MedicoRequest request) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));

        medico.setCrm(request.crm());
        medico.setNome(request.nome());
        medico.setEmail(request.email());
        medico.setTelefone(request.telefone());
        medico.setEspecialidade(request.especialidade());

        Departamento departamento = departamentoRepository.findById(request.departamentoId())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe departamento com esse ID!"));

        medico.setDepartamento(departamento);

        return mapper.toDTO(medicoRepository.save(medico));
    }
}
