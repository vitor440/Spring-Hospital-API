package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.exception.AcessoNegadoException;
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
    private final SecurityService securityService;


    @Transactional
    public ResultadoConsultaResponse gerarResultadoDaConsulta(ResultadoConsultaRequest request, Long consultaId) {

        Usuario usuario = securityService.getUsuarioLogado();
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);
        Medico medico = consulta.getMedico();

        validaSeUsuarioTemPermissaoParaGerenciarResultado(medico, usuario);

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

            if(rs.getPrescricao().getMedicamentos() != null) {
                for (Medicamento medicamento : rs.getPrescricao().getMedicamentos()) {
                    medicamento.setPrescricao(rs.getPrescricao());
                }
            }
        }

        consultaRepository.modificaStatusConsulta(consultaId, StatusConsulta.REALIZADA);
        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }


    public ResultadoConsultaResponse atualizarResultadoDaConsulta(Long id, ResultadoConsultaRequest request) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);

        Usuario usuario = securityService.getUsuarioLogado();
        Medico medico = rs.getConsulta().getMedico();

        validaSeUsuarioTemPermissaoParaGerenciarResultado(medico, usuario);

        rs.setSintomas(request.sintomas());
        rs.setNotas(request.notas());
        rs.setTratamento(request.tratamento());
        rs.setDataRetorno(request.dataRetorno());
        rs.setDiagnostico(request.diagnostico());

        if(rs.getPrescricao() != null) {
            rs.getPrescricao().setComentarios(request.prescricao().comentarios());
            rs.getPrescricao().setDataPrescricao(request.prescricao().dataPrescricao());


            if(rs.getPrescricao().getMedicamentos() != null) {
                rs.getPrescricao().getMedicamentos().clear();

                for (MedicamentoRequest medicamentoRequest : request.prescricao().medicamentos()) {
                    Medicamento medicamento = medicamentoMapper.toEntity(medicamentoRequest);
                    medicamento.setPrescricao(rs.getPrescricao());
                    rs.getPrescricao().getMedicamentos().add(medicamento);
                }
            }
        }
        return mapper.toDTO(resultadoConsultaRepository.save(rs));
    }


    public void deletarResultadoDaConsulta(Long id) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);
        Usuario usuario = securityService.getUsuarioLogado();
        Medico medico = rs.getConsulta().getMedico();

        validaSeUsuarioTemPermissaoParaGerenciarResultado(medico, usuario);
        resultadoConsultaRepository.delete(rs);
    }


    public ResultadoConsultaResponse obterResultadoPeloIdDaConsulta(Long consultaId) {
        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(consultaId);

        if(consulta.getResultadoConsulta() == null) {
            throw new RegistroNaoEncontradoException("Não existe resultado para essa consulta!");
        }

        return mapper.toDTO(consulta.getResultadoConsulta());
    }

    public ResultadoConsultaResponse obterResultadoPeloId(Long id) {
        ResultadoConsulta rs = obterResultadoConsultaPeloIdOuLancarExcecao(id);
        Usuario usuario = securityService.getUsuarioLogado();

        if(usuario.getRoles().contains("MEDICO")) {
            Medico medico = rs.getConsulta().getMedico();
            validaSeUsuarioTemPermissaoParaGerenciarResultado(medico, usuario);
        }

        return mapper.toDTO(rs);
    }



    private Consulta obterConsultaPeloIdOuLancarExcecao(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }

    private ResultadoConsulta obterResultadoConsultaPeloIdOuLancarExcecao(Long id) {
        return resultadoConsultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe resultado de consulta com esse ID!"));
    }

    private static void validaSeUsuarioTemPermissaoParaGerenciarResultado(Medico medico, Usuario usuario) {
        if(!medico.getUsuario().getId().equals(usuario.getId())) {
            throw new AcessoNegadoException("médico não autorizado!");
        }
    }
}
