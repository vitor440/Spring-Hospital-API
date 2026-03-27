package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.ResultadoConsultaRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import com.gerenciamento_hospitalar.dto.response.ResultadoConsultaResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.ResultadoConsultaMapper;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Prescricao;
import com.gerenciamento_hospitalar.model.ResultadoConsulta;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.PrescricaoRepository;
import com.gerenciamento_hospitalar.repository.ResultadoConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultadoConsultaService {

    private final ResultadoConsultaRepository resultadoConsultaRepository;
    private final ConsultaRepository consultaRepository;
    private final PrescricaoService prescricaoService;
    private final MedicamentoService medicamentoService;
    private final ResultadoConsultaMapper mapper;

    public ResultadoConsultaResponse create(ResultadoConsultaRequest request) {
        ResultadoConsulta rs = mapper.toEntity(request);

        Consulta consulta = obterConsultaPeloIdOuLancarExcecao(request.consultaId());
        rs.setConsulta(consulta);
        rs.setMedicoId(consulta.getMedico().getId());
        rs.setPacienteId(consulta.getPaciente().getId());

        PrescricaoResponse prescricao = prescricaoService.create(request.prescricao());

        Prescricao entidade = new Prescricao();
        entidade.setId(prescricao.id());
        entidade.setMedicoId(prescricao.medicoId());
        entidade.setPacienteId(prescricao.pacienteId());
        entidade.setConsulta(consulta);
        entidade.setComentarios(prescricao.comentarios());
        entidade.setMedicamentos(prescricao.medicamentos());
        rs.setPrescricao(prescricao);
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

    private Consulta obterConsultaPeloIdOuLancarExcecao(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }

    private ResultadoConsulta obterResultadoConsultaPeloIdOuLancarExcecao(Long id) {
        return resultadoConsultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }


}
