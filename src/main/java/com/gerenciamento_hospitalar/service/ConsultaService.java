package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.ConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ConsultaResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.ConsultaMapper;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Medico;
import com.gerenciamento_hospitalar.model.Paciente;
import com.gerenciamento_hospitalar.model.StatusConsulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicoRepository;
import com.gerenciamento_hospitalar.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaMapper mapper;

    public ConsultaResponse agendarConsulta(ConsultaRequest request) {
        Consulta consulta = mapper.toEntity(request);

        Medico medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));

        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));

        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setStatus(StatusConsulta.AGENDADA);

        return mapper.toDTO(consultaRepository.save(consulta));
    }
}
