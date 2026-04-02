package com.gerenciamento_hospitalar.controller;

import com.gerenciamento_hospitalar.dto.request.DisponibilidadeMedicoRequest;
import com.gerenciamento_hospitalar.dto.response.DisponibilidadeMedicoResponse;
import com.gerenciamento_hospitalar.service.DisponibilidadeMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/disponibilidade")
@RequiredArgsConstructor
public class DisponibilidadeMedicoController {

    private final DisponibilidadeMedicoService service;

    @PostMapping
    public ResponseEntity<DisponibilidadeMedicoResponse> addDisponibilidadeMedico(@RequestBody DisponibilidadeMedicoRequest request) {
        DisponibilidadeMedicoResponse response = service.addDisponibilidadeMedico2(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisponibilidadeMedicoResponse> atualizarDisponibilidade(@PathVariable("id") Long id,
                                                                                  @RequestBody DisponibilidadeMedicoRequest request) {
        return ResponseEntity.ok(service.atualizarDisponibilidadeMedico2(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisponibilidadeMedicoResponse> obterPeloId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.obterPeloId(id));
    }

    @GetMapping
    public ResponseEntity<Page<DisponibilidadeMedicoResponse>> listarDisponibilidades(
            @RequestParam(value = "pagina", defaultValue = "0") int pagina,
            @RequestParam(value = "tamanho", defaultValue = "5") int tamanho) {

        return ResponseEntity.ok(service.listarDisponibilidades2(pagina, tamanho));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPeloId(@PathVariable("id") Long id) {
        service.deletarPeloId(id);
        return ResponseEntity.noContent().build();
    }
}
