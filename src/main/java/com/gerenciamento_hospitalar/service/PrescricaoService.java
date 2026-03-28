package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.request.PrescricaoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.PrescricaoMapper;
import com.gerenciamento_hospitalar.mapper.PrescricaoMedicamentoMapper;
import com.gerenciamento_hospitalar.model.Consulta;
import com.gerenciamento_hospitalar.model.Medicamento;
import com.gerenciamento_hospitalar.model.Prescricao;
import com.gerenciamento_hospitalar.model.PrescricaoMedicamento;
import com.gerenciamento_hospitalar.repository.ConsultaRepository;
import com.gerenciamento_hospitalar.repository.MedicamentoRepository;
import com.gerenciamento_hospitalar.repository.PrescricaoMedicamentoRepository;
import com.gerenciamento_hospitalar.repository.PrescricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction;

@Service
@RequiredArgsConstructor
public class PrescricaoService {

    private final PrescricaoRepository prescricaoRepository;
    private final ConsultaRepository consultaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final PrescricaoMapper mapper;
    private final PrescricaoMedicamentoMapper prescricaoMedicamentoMapper;
    
    /*
    *
    * {
    *   "consultaId": <Long>,
    *   "dataPrescricao": <LocalDate>,
    *   "comentarios": <String>,
    *   "medicamentos":
    *   {
    *       "prescricaoId": <Long>,
    *       "medicamentoId": <Long>,
    *       "dosagem": <String>,
    *       "frequencia": <String>,
    *       "duracao": <String>
    *   },
    *   {
     *       "prescricaoId": <Long>,
     *       "medicamentoId": <Long>,
     *       "dosagem": <String>,
     *       "frequencia": <String>,
     *       "duracao": <String>
     *   }
    * }
    *
    * */

    public PrescricaoResponse create(PrescricaoRequest request) {
        Prescricao prescricao = mapper.toEntity(request);
        Consulta consulta = obterConsultaPorIdOuLancarExcecao(request.consultaId());
        prescricao.setConsulta(consulta);
        prescricao.setMedicoId(consulta.getMedico().getId());
        prescricao.setPacienteId(consulta.getPaciente().getId());

        prescricao.setMedicamentos(new ArrayList<>());
        for (PrescricaoMedicamentoRequest prescMedicamento : request.medicamentos()) {
            PrescricaoMedicamento prescricaoMedicamento = prescricaoMedicamentoMapper.toEntity(prescMedicamento);

            Medicamento medicamento = obterMedicamentoPeloIdOuLancarExcecao(prescMedicamento.medicamentoId());
            prescricaoMedicamento.setMedicamento(medicamento);
            prescricaoMedicamento.setPrescricao(prescricao);
            prescricao.getMedicamentos().add(prescricaoMedicamento);

        }

        return mapper.toDTO(prescricaoRepository.save(prescricao));
    }



    public PrescricaoResponse obterPeloId(Long id) {
        Prescricao prescricao = obterPrescricaoPorIdOuLancarExcecao(id);
        return mapper.toDTO(prescricao);
    }

    public Page<PrescricaoResponse> listarPrescricoes(int pagina, int tamanho, String direction) {
        var sort = direction.equalsIgnoreCase("ASC")? Direction.ASC: Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "dataPrescricao");

        return prescricaoRepository.findAll( pageable).map(mapper::toDTO);
    }

    public List<PrescricaoResponse> listarPrescricoesPeloIdDaConsulta(Long id) {
        List<Prescricao> prescricoes = prescricaoRepository.listarPrescricoesPeloIdDaConsulta(id);
        return prescricoes.stream().map(mapper::toDTO).toList();
    }



    public PrescricaoResponse atualizar(Long id, PrescricaoRequest request) {
        Prescricao prescricao = obterPrescricaoPorIdOuLancarExcecao(id);
        Consulta consulta = obterConsultaPorIdOuLancarExcecao(request.consultaId());

        prescricao.setConsulta(consulta);
        prescricao.setMedicoId(consulta.getMedico().getId());
        prescricao.setPacienteId(consulta.getPaciente().getId());
        prescricao.setDataPrescricao(request.dataPrescricao());
        prescricao.setComentarios(request.comentarios());

        return mapper.toDTO(prescricaoRepository.save(prescricao));
    }

    public void deletarPeloId(Long id) {
        Prescricao prescricao = obterPrescricaoPorIdOuLancarExcecao(id);
        prescricaoRepository.delete(prescricao);
    }

    private Prescricao obterPrescricaoPorIdOuLancarExcecao(Long id) {
        return prescricaoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe prescrição com esse ID!"));
    }


    private Consulta obterConsultaPorIdOuLancarExcecao(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe consulta com esse ID!"));
    }

    private Medicamento obterMedicamentoPeloIdOuLancarExcecao(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe medicamento com esse ID!"));
    }
}
