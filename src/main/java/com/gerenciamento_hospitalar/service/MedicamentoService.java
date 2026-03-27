package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicamentoResponse;
import com.gerenciamento_hospitalar.exception.RegistroNaoEncontradoException;
import com.gerenciamento_hospitalar.mapper.MedicamentoMapper;
import com.gerenciamento_hospitalar.model.Medicamento;
import com.gerenciamento_hospitalar.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicamentoService implements IMedicamentoService{

    private final MedicamentoRepository medicamentoRepository;
    private final MedicamentoMapper mapper;

    @Override
    public MedicamentoResponse create(MedicamentoRequest request) {
        Medicamento medicamento = mapper.toEntity(request);
        return mapper.toDTO(medicamentoRepository.save(medicamento));
    }

    @Override
    public MedicamentoResponse getById(Long id) {
        return mapper.toDTO(obterMedicamentoPeloIdOuLancarExcecao(id));
    }

    @Override
    public Page<MedicamentoResponse> listar(int pagina, int tamanho, String direction) {
        var sort = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pagina, tamanho, sort, "nome");
        return medicamentoRepository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public MedicamentoResponse update(Long id, MedicamentoRequest request) {
        Medicamento medicamento = obterMedicamentoPeloIdOuLancarExcecao(id);

        medicamento.setNome(request.nome());
        medicamento.setTipo(request.tipo());
        medicamento.setMarca(request.marca());
        medicamento.setDescricao(request.descricao());
        medicamento.setEstoque(request.estoque());

        return mapper.toDTO(medicamentoRepository.save(medicamento));
    }

    @Override
    public void delete(Long id) {
        Medicamento medicamento = obterMedicamentoPeloIdOuLancarExcecao(id);
        medicamentoRepository.delete(medicamento);
    }

    private Medicamento obterMedicamentoPeloIdOuLancarExcecao(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Não existe medicamento com esse ID!"));
    }
}
