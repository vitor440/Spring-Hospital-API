package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.DepartamentoRequest;
import com.gerenciamento_hospitalar.dto.response.DepartamentoResponse;
import com.gerenciamento_hospitalar.service.DepartamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/departamentos")
@RequiredArgsConstructor
public class DepartamentoController {

    private final DepartamentoService service;

    @PostMapping
    public ResponseEntity<DepartamentoResponse> addDepartamento(@RequestBody @Valid DepartamentoRequest request) {
        DepartamentoResponse response = service.addDepartamento(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> atualizarDepartamento(@PathVariable("id") Long id, @RequestBody @Valid DepartamentoRequest request) {
        return ResponseEntity.ok(service.atualizarDepartamento(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDepartamentoPeloId(@PathVariable("id") Long id) {
        service.deletarDepartamentoPeloId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> obterDepartamentoPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterDepartamentoPeloId(id));
    }

    @GetMapping
    public ResponseEntity<Page<DepartamentoResponse>> listarDepartamentos(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "localizacao", required = false) String localizacao,
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(service.listarDepartamentos(nome, localizacao, pagina, tamanho, direction));
    }


}
