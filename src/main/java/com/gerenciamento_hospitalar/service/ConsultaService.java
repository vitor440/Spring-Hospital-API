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
import com.gerenciamento_hospitalar.validator.ConsultaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.*;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final ConsultaValidator validator;
    private final ConsultaMapper mapper;

    public ConsultaResponse agendarConsulta(ConsultaRequest request) {
        Consulta consulta = mapper.toEntity(request);

        Medico medico = obterMedicoPeloIdOuLancarExcecao(request.medicoId());

        Paciente paciente = obterPacientePeloIdOuLancarExcecao(request.pacienteId());

        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setDiaSemana(consulta.getData().getDayOfWeek().getValue());

        validator.validar(consulta);
        return mapper.toDTO(consultaRepository.save(consulta));
    }

    public Page<ConsultaResponse> listarConsultas(int pagina, int tamanho, String direction) {

        var sort = direction.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "data");
        Page<Consulta> consultas = consultaRepository.findAll(pageable);

        return consultas.map(mapper::toDTO);
    }

    public ConsultaResponse atualizarConsulta(Long id, ConsultaRequest request) {
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(id);

        consulta.setProposito(request.proposito());
        consulta.setData(request.data());
        consulta.setHora(request.hora());

        Medico medico = obterMedicoPeloIdOuLancarExcecao(request.medicoId());
        Paciente paciente = obterPacientePeloIdOuLancarExcecao(request.pacienteId());

        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setDiaSemana(consulta.getData().getDayOfWeek().getValue());

        validator.validar(consulta);
        return mapper.toDTO(consultaRepository.save(consulta));
    }


    public ConsultaResponse obterConsultaPeloId(Long id) {
        return mapper.toDTO(obterConsultaPeloIdOuLancarExcecao(id));
    }


    public void deletarConsultaPeloId(Long id) {
        consultaRepository.delete(obterConsultaPeloIdOuLancarExcecao(id));
    }


    private Medico obterMedicoPeloIdOuLancarExcecao(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe médico com esse ID!"));
    }

    private Paciente obterPacientePeloIdOuLancarExcecao(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe paciente com esse ID!"));
    }

    private Consulta obterConsultaPeloIdOuLancarExcecao(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }
}


