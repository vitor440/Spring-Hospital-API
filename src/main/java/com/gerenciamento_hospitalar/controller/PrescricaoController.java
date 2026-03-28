package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.PrescricaoRequest;
import com.gerenciamento_hospitalar.dto.response.PrescricaoResponse;
import com.gerenciamento_hospitalar.service.PrescricaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/prescricoes")
@RequiredArgsConstructor
public class PrescricaoController {

    private final PrescricaoService service;

//    @PostMapping
//    public ResponseEntity<PrescricaoResponse> create(@RequestBody @Valid PrescricaoRequest request) {
//        PrescricaoResponse response = service.create(request);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(response.id())
//                .toUri();
//
//        return ResponseEntity.created(location).body(response);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<PrescricaoResponse> atualizar(@PathVariable("id") Long id, @RequestBody @Valid PrescricaoRequest request) {
//        return ResponseEntity.ok(service.atualizar(id, request));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescricaoResponse> obterPrescricaoPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterPeloId(id));
    }

    @GetMapping("/consulta/{id}")
    public ResponseEntity<List<PrescricaoResponse>> listarPrescricoesPeloIdDaConsulta(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.listarPrescricoesPeloIdDaConsulta(id));
    }

    @GetMapping
    public ResponseEntity<Page<PrescricaoResponse>> listarPrescricoes(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(service.listarPrescricoes(pagina, tamanho, direction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPrescricaoPeloId(@PathVariable("id") Long id) {
        service.deletarPeloId(id);

        return ResponseEntity.noContent().build();
    }
}
