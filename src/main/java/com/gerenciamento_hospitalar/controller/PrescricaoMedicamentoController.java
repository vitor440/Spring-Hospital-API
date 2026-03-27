package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.PrescricaoMedicamentoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoMedicamentoResponse;
import com.gerenciamento_hospitalar.service.PrescricaoMedicamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/prescricoes-medicamentos")
@RequiredArgsConstructor
public class PrescricaoMedicamentoController {

    private final PrescricaoMedicamentoService service;

    @PostMapping
    public ResponseEntity<PrescricaoMedicamentoResponse> create(@RequestBody PrescricaoMedicamentoRequest request) {
        PrescricaoMedicamentoResponse response = service.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescricaoMedicamentoResponse> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<PrescricaoMedicamentoResponse>> listar(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(service.listar(pagina, tamanho, direction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescricaoMedicamentoResponse> update(@PathVariable("id") Long id, @RequestBody PrescricaoMedicamentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PrescricaoMedicamentoResponse> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


}
