package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.MedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.MedicamentoResponse;
import org.springframework.data.domain.Page;

public interface IMedicamentoService {

    public MedicamentoResponse create(MedicamentoRequest request);

    public MedicamentoResponse getById(Long id);

    public Page<MedicamentoResponse> listar(int pagina, int tamanho, String direction);

    public MedicamentoResponse update(Long id, MedicamentoRequest request);

    public void delete(Long id);
}
