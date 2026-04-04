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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultadoConsultaService {

    private final ResultadoConsultaRepository resultadoConsultaRepository;
    private final ConsultaRepository consultaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ResultadoConsultaMapper mapper;

    public ResultadoConsultaResponse create(ResultadoConsultaRequest request, Long consultaId) {

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

            prescricaoMedicamento.setNome(prescMedicamento.nome());
            prescricaoMedicamento.setTipo(prescMedicamento.tipo());
            prescricaoMedicamento.setDescricao(prescMedicamento.descricao());
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

    public void delete(Long consultaId) {
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        ResultadoConsulta resultadoConsulta = consulta.getResultadoConsulta();
        consulta.setResultadoConsulta(null);
        consulta.setPrescricao(null);
        resultadoConsulta.setConsulta(null);
        resultadoConsultaRepository.delete(resultadoConsulta);
    }


    public ResultadoConsultaResponse update(Long consultaId, ResultadoConsultaRequest request) {
        Consulta consulta =obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        ResultadoConsulta resultadoConsulta = consulta.getResultadoConsulta();
        resultadoConsulta.setSintomas(request.sintomas());
        resultadoConsulta.setDiagnostico(request.diagnostico());
        resultadoConsulta.setDataRetorno(request.dataRetorno());
        resultadoConsulta.setNotas(request.notas());
        resultadoConsulta.setTratamento(request.tratamento());

        Prescricao prescricao = resultadoConsulta.getPrescricao();
        prescricao.setDataPrescricao(request.prescricao().dataPrescricao());
        prescricao.setComentarios(request.prescricao().comentarios());

        List<PrescricaoMedicamentoRequest> medicamentosReq = request.prescricao().medicamentos();

        atualizarMedicamentos(medicamentosReq, prescricao);

        return mapper.toDTO(resultadoConsultaRepository.save(resultadoConsulta));
    }

    private void atualizarMedicamentos(List<PrescricaoMedicamentoRequest> medicamentosReq, Prescricao prescricao) {

        prescricao.getMedicamentos().clear();

        for (PrescricaoMedicamentoRequest medicamentoRequest : medicamentosReq) {
            PrescricaoMedicamento medicamento = new PrescricaoMedicamento();
            medicamento.setNome(medicamentoRequest.nome());
            medicamento.setTipo(medicamentoRequest.tipo());
            medicamento.setDescricao(medicamentoRequest.descricao());
            medicamento.setDosagem(medicamentoRequest.dosagem());
            medicamento.setFrequencia(medicamentoRequest.frequencia());
            medicamento.setDuracao(medicamentoRequest.duracao());

            medicamento.setPrescricao(prescricao);
            prescricao.getMedicamentos().add(medicamento);
        }
    }

    public ResultadoConsultaResponse getByConsultaId(Long consultaId) {
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        return mapper.toDTO(consulta.getResultadoConsulta());
    }

    public void deleteByConsultaId(Long consultaId) {
        Consulta consulta =obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        resultadoConsultaRepository.delete(consulta.getResultadoConsulta());
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
