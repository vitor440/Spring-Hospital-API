package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.PrescricaoMapper;
import com.gerenciamento_hospitalar.mapper.PrescricaoMedicamentoMapper;
import com.gerenciamento_hospitalar.mapper.ResultadoConsultaMapper;
import com.gerenciamento_hospitalar.model.*;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicamentoRepository;
import com.gerenciamento_hospitalar.repository.ResultadoConsultaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultadoConsultaService {

    private final ResultadoConsultaRepository resultadoConsultaRepository;
    private final ConsultaRepository consultaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ResultadoConsultaMapper mapper;
    private final PrescricaoMedicamentoMapper medicamentoMapper;


    @Transactional
    public ResultadoConsultaResponse gerarResultadoDaConsulta(ResultadoConsultaRequest request, Long consultaId) {

        ResultadoConsulta rs = mapper.toEntity(request);
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);
        rs.setConsulta(consulta);
        rs.setMedicoId(consulta.getMedico().getId());
        rs.setPacienteId(consulta.getPaciente().getId());

        rs.getPrescricao().setConsulta(consulta);
        rs.getPrescricao().setMedicoId(consulta.getMedico().getId());
        rs.getPrescricao().setPacienteId(consulta.getPaciente().getId());

        for (PrescricaoMedicamento medicamento : rs.getPrescricao().getMedicamentos()) {
            medicamento.setPrescricao(rs.getPrescricao());
        }

        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }



    public void deletarResultadoDaConsulta(Long consultaId) {
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


    public ResultadoConsultaResponse atualizarResultadoDaConsulta(Long consultaId, ResultadoConsultaRequest request) {
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

    public ResultadoConsultaResponse atualizarResultadoDaConsulta2(Long consultaId, ResultadoConsultaRequest request) {
        Consulta consulta =obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        ResultadoConsulta rs = consulta.getResultadoConsulta();
        rs.setSintomas(request.sintomas());
        rs.setNotas(request.notas());
        rs.setTratamento(request.tratamento());
        rs.setDataRetorno(request.dataRetorno());
        rs.setDiagnostico(request.diagnostico());

        rs.getPrescricao().setComentarios(request.prescricao().comentarios());
        rs.getPrescricao().setDataPrescricao(request.prescricao().dataPrescricao());

        rs.getPrescricao().getMedicamentos().clear();

        for (PrescricaoMedicamentoRequest medicamentoRequest : request.prescricao().medicamentos()) {
            PrescricaoMedicamento medicamento = medicamentoMapper.toEntity(medicamentoRequest);
            medicamento.setPrescricao(rs.getPrescricao());
            rs.getPrescricao().getMedicamentos().add(medicamento);
        }

        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }

    public ResultadoConsultaResponse obterResultadoPeloIdDaConsulta(Long consultaId) {
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
