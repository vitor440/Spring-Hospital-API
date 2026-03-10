package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PacienteRequest;
import com.gerenciamento_hospitalar.dto.response.PacienteResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.PacienteMapper;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import com.gerenciamento_hospitalar.validator.PacienteValidator;
import lombok.RequiredArgsConstructor;
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
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe paciente com esse ID!"));

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
}
