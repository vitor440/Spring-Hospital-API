package com.gerenciamento_hospitalar.service;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoMedicamentoResponse;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import org.springframework.data.domain.Page;

public interface IPrescricaoMedicamentoService {

    public PrescricaoMedicamentoResponse create(PrescricaoMedicamentoRequest request);

    public PrescricaoMedicamentoResponse getById(Long id);

    public Page<PrescricaoMedicamentoResponse> listar(int pagina, int tamanho, String direction);

    public PrescricaoMedicamentoResponse update(Long id, PrescricaoMedicamentoRequest request);

    public void delete(Long id);
}
