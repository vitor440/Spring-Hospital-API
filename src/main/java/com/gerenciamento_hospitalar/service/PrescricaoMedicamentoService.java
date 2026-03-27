package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoMedicamentoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.PrescricaoMedicamentoMapper;
import com.gerenciamento_hospitalar.model.Medicamento;
import com.gerenciamento_hospitalar.model.Prescricao;
import com.gerenciamento_hospitalar.model.PrescricaoMedicamento;
import com.gerenciamento_hospitalar.repository.MedicamentoRepository;
import com.gerenciamento_hospitalar.repository.PrescricaoMedicamentoRepository;
import com.gerenciamento_hospitalar.repository.PrescricaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescricaoMedicamentoService implements IPrescricaoMedicamentoService{

    private final PrescricaoMedicamentoRepository prescricaoMedicamentoRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final PrescricaoRepository prescricaoRepository;
    private final PrescricaoMedicamentoMapper mapper;


    @Override
    public PrescricaoMedicamentoResponse create(PrescricaoMedicamentoRequest request) {
        PrescricaoMedicamento prescricaoMedicamento = mapper.toEntity(request);

        Prescricao prescricao = obterPrescricaoPorIdOuLancarExcecao(request.prescricaoId());
        Medicamento medicamento = obterMedicamentoPeloIdOuLancarExcecao(request.medicamentoId());
        prescricaoMedicamento.setPrescricao(prescricao);
        prescricaoMedicamento.setMedicamento(medicamento);

        return mapper.toDTO(prescricaoMedicamentoRepository.save(prescricaoMedicamento));
    }

    @Override
    public PrescricaoMedicamentoResponse getById(Long id) {
        return mapper.toDTO(obterPrescricaoMedicamentoPeloIdOuLancarExcecao(id));
    }

    @Override
    public Page<PrescricaoMedicamentoResponse> listar(int pagina, int tamanho, String direction) {
        var sort = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "nome");

        return prescricaoMedicamentoRepository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public PrescricaoMedicamentoResponse update(Long id, PrescricaoMedicamentoRequest request) {
        PrescricaoMedicamento prescricaoMedicamento = obterPrescricaoMedicamentoPeloIdOuLancarExcecao(id);

        Prescricao prescricao = obterPrescricaoPorIdOuLancarExcecao(request.prescricaoId());
        Medicamento medicamento = obterMedicamentoPeloIdOuLancarExcecao(request.medicamentoId());
        prescricaoMedicamento.setPrescricao(prescricao);
        prescricaoMedicamento.setMedicamento(medicamento);
        prescricaoMedicamento.setDosagem(request.dosagem());
        prescricaoMedicamento.setDuracao(request.duracao());
        prescricaoMedicamento.setFrequencia(request.frequencia());

        return mapper.toDTO(prescricaoMedicamentoRepository.save(prescricaoMedicamento));
    }

    @Override
    public void delete(Long id) {
        PrescricaoMedicamento prescricaoMedicamento = obterPrescricaoMedicamentoPeloIdOuLancarExcecao(id);
        prescricaoMedicamentoRepository.delete(prescricaoMedicamento);
    }


    private PrescricaoMedicamento obterPrescricaoMedicamentoPeloIdOuLancarExcecao(Long id) {
        return prescricaoMedicamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe prescrição de medicamento com esse ID!"));
    }

    private Prescricao obterPrescricaoPorIdOuLancarExcecao(Long id) {
        return prescricaoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe prescrição com esse ID!"));
    }

    private Medicamento obterMedicamentoPeloIdOuLancarExcecao(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe medicamento com esse ID!"));
    }
}
