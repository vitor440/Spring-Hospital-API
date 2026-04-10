package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.exception.RegistroDuplicadoException;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.MedicamentoMapper;
import com.gerenciamento_hospitalar.mapper.ResultadoConsultaMapper;
import com.gerenciamento_hospitalar.model.*;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.ResultadoConsultaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultadoConsultaService {

    private final ResultadoConsultaRepository resultadoConsultaRepository;
    private final ConsultaRepository consultaRepository;
    private final ResultadoConsultaMapper mapper;
    private final MedicamentoMapper medicamentoMapper;


    @Transactional
    public ResultadoConsultaResponse gerarResultadoDaConsulta(ResultadoConsultaRequest request, Long consultaId) {

        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() != null) {
            throw new RegistroDuplicadoException("Esta consulta já possui resultado!");
        }

        ResultadoConsulta rs = mapper.toEntity(request);
        rs.setConsulta(consulta);
        rs.setMedicoId(consulta.getMedico().getId());
        rs.setPacienteId(consulta.getPaciente().getId());

        if(rs.getPrescricao() != null) {
            rs.getPrescricao().setConsulta(consulta);
            rs.getPrescricao().setMedicoId(consulta.getMedico().getId());
            rs.getPrescricao().setPacienteId(consulta.getPaciente().getId());

            for (Medicamento medicamento : rs.getPrescricao().getMedicamentos()) {
                medicamento.setPrescricao(rs.getPrescricao());
            }
        }

        consultaRepository.modificaStatusConsulta(consultaId, StatusConsulta.REALIZADA);
        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }




    public ResultadoConsultaResponse atualizarResultadoDaConsulta(Long consultaId, ResultadoConsultaRequest request) {
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

        if(rs.getPrescricao() != null) {
            rs.getPrescricao().setComentarios(request.prescricao().comentarios());
            rs.getPrescricao().setDataPrescricao(request.prescricao().dataPrescricao());

            rs.getPrescricao().getMedicamentos().clear();

            for (MedicamentoRequest medicamentoRequest : request.prescricao().medicamentos()) {
                Medicamento medicamento = medicamentoMapper.toEntity(medicamentoRequest);
                medicamento.setPrescricao(rs.getPrescricao());
                rs.getPrescricao().getMedicamentos().add(medicamento);
            }
        }
        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }

    public ResultadoConsultaResponse atualizarResultadoDaConsulta2(Long id, ResultadoConsultaRequest request) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);

        rs.setSintomas(request.sintomas());
        rs.setNotas(request.notas());
        rs.setTratamento(request.tratamento());
        rs.setDataRetorno(request.dataRetorno());
        rs.setDiagnostico(request.diagnostico());

        if(rs.getPrescricao() != null) {
            rs.getPrescricao().setComentarios(request.prescricao().comentarios());
            rs.getPrescricao().setDataPrescricao(request.prescricao().dataPrescricao());

            rs.getPrescricao().getMedicamentos().clear();

            for (MedicamentoRequest medicamentoRequest : request.prescricao().medicamentos()) {
                Medicamento medicamento = medicamentoMapper.toEntity(medicamentoRequest);
                medicamento.setPrescricao(rs.getPrescricao());
                rs.getPrescricao().getMedicamentos().add(medicamento);
            }
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

    public void deletarResultadoDaConsulta2(Long id) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);

        resultadoConsultaRepository.delete(rs);
    }




    public ResultadoConsultaResponse obterResultadoPeloIdDaConsulta(Long consultaId) {
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        return mapper.toDTO(consulta.getResultadoConsulta());
    }

    public ResultadoConsultaResponse obterResultadoPeloIdDaConsulta2(Long id) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);
        return mapper.toDTO(rs);
    }



    private Consulta obterConsultaPeloIdOuLancarExcecao(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }

    private ResultadoConsulta obterResultadoConsultaPeloIdOuLancarExcecao(Long id) {
        return resultadoConsultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }
}
