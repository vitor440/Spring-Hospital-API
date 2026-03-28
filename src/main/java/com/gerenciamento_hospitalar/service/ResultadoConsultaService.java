package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.ResultadoConsultaMapper;
import com.gerenciamento_hospitalar.model.*;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicamentoRepository;
import com.gerenciamento_hospitalar.repository.ResultadoConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResultadoConsultaService {

    private final ResultadoConsultaRepository resultadoConsultaRepository;
    private final ConsultaRepository consultaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ResultadoConsultaMapper mapper;

    public ResultadoConsultaResponse create(ResultadoConsultaRequest request, Long consultaId) {
        //PrescricaoRequest prescricaoRequest = request.prescricao();
        //prescricaoService.create(prescricaoRequest);

        ResultadoConsulta rs = mapper.toEntity(request);
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);
        rs.setConsulta(consulta);
        rs.setMedicoId(consulta.getMedico().getId());
        rs.setPacienteId(consulta.getPaciente().getId());

        Prescricao prescricao = rs.getPrescricao();
        prescricao.setConsulta(consulta);
        prescricao.setMedicoId(consulta.getMedico().getId());
        prescricao.setPacienteId(consulta.getPaciente().getId());

        prescricao.setMedicamentos(new ArrayList<>());
        for (PrescricaoMedicamentoRequest prescMedicamento : request.prescricao().medicamentos()) {
            PrescricaoMedicamento prescricaoMedicamento = new PrescricaoMedicamento();

            Medicamento medicamento = obterMedicamentoPeloIdOuLancarExcecao(prescMedicamento.medicamentoId());
            prescricaoMedicamento.setMedicamento(medicamento);
            prescricaoMedicamento.setDosagem(prescMedicamento.dosagem());
            prescricaoMedicamento.setFrequencia(prescMedicamento.frequencia());
            prescricaoMedicamento.setDuracao(prescMedicamento.duracao());

            prescricaoMedicamento.setPrescricao(prescricao);
            prescricao.getMedicamentos().add(prescricaoMedicamento);

        }

        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }

    public ResultadoConsultaResponse getById(Long id) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);
        return mapper.toDTO(rs);
    }

    public void delete(Long id) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);
        resultadoConsultaRepository.delete(rs);
    }

    public ResultadoConsultaResponse getByConsultaId(Long consultaId) {
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);

        return mapper.toDTO(consulta.getResultadoConsulta());
    }

    private Consulta obterConsultaPeloIdOuLancarExcecao(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }

    private ResultadoConsulta obterResultadoConsultaPeloIdOuLancarExcecao(Long id) {
        return resultadoConsultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }

    private Medicamento obterMedicamentoPeloIdOuLancarExcecao(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe medicamento com esse ID!"));
    }
}
